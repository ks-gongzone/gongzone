package com.gongzone.central.party.after.service.admin;

import static com.gongzone.central.common.constants.ConstantsString.ADMIN_POINT_NO;
import static com.gongzone.central.common.constants.ConstantsString.MESSAGE_ALERT_PARTY_SETTLEMENT_COMPLETE;
import static com.gongzone.central.utils.StatusCode.STATUS_SETTLEMENT_COMPLETE;
import static com.gongzone.central.utils.TypeCode.TYPE_POINT_DECREASE_ADMIN_PARTY_SETTLEMENT;
import static com.gongzone.central.utils.TypeCode.TYPE_POINT_INCREASE_SETTLEMENT;
import static com.gongzone.central.utils.TypeCode.party;

import com.gongzone.central.member.alertSSE.domain.AlertSSE;
import com.gongzone.central.member.alertSSE.mapper.AlertSSEMapper;
import com.gongzone.central.party.after.domain.SettlementDetail;
import com.gongzone.central.party.after.mapper.PartyAfterMapper;
import com.gongzone.central.point.domain.PointHistory;
import com.gongzone.central.point.mapper.PointHistoryMapper;
import com.gongzone.central.point.mapper.PointMapper;
import com.gongzone.central.utils.MySqlUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminPartyAfterServiceImpl implements AdminPartyAfterService {

	private final PointMapper pointMapper;
	private final PartyAfterMapper partyAfterMapper;
	private final PointHistoryMapper pointHistoryMapper;
	private final AlertSSEMapper alertSSEMapper;


	@Override
	@Transactional
	public void settlement(String partyNo) {
		// 1. (관리자) 포인트 차감 내역 삽입
		String adminHistoryPk = MySqlUtil.generatePrimaryKey(pointHistoryMapper.getLastIndex());
		int adminCurrentPoint = pointMapper.getCurrentPoint(ADMIN_POINT_NO.toString());
		int pointChange = partyAfterMapper.getSettlementPrice(partyNo);
		PointHistory adminHistory = PointHistory.builder()
												.pointHistoryNo(adminHistoryPk)
												.memberPointNo(ADMIN_POINT_NO.toString())
												.pointHistoryBefore(adminCurrentPoint)
												.pointHistoryChange(-pointChange)
												.pointHistoryAfter(adminCurrentPoint)
												.type(TYPE_POINT_DECREASE_ADMIN_PARTY_SETTLEMENT.getCode())
												.build();
		pointHistoryMapper.insert(adminHistory);

		// 1-1. (관리자) 포인트 차감
		pointMapper.update(ADMIN_POINT_NO.toString(), -pointChange);

		// 2. (사용자) 포인트 증가 내역 삽입
		String memberHistoryPk = MySqlUtil.generatePrimaryKey(pointHistoryMapper.getLastIndex());
		String partyMemberNo = partyAfterMapper.getLeaderPartyMemberNo(partyNo);
		String memberNo = partyAfterMapper.getMemberNoByPartyMemberNo(partyMemberNo);
		String memberPointNo = pointMapper.getMemberPointNo(memberNo);
		int memberCurrentPoint = pointMapper.getCurrentPoint(memberPointNo);
		PointHistory memberHistory = PointHistory.builder()
												 .pointHistoryNo(memberHistoryPk)
												 .memberPointNo(memberPointNo)
												 .pointHistoryBefore(memberCurrentPoint)
												 .pointHistoryChange(pointChange)
												 .pointHistoryAfter(memberCurrentPoint)
												 .type(TYPE_POINT_INCREASE_SETTLEMENT.getCode())
												 .build();
		pointHistoryMapper.insert(memberHistory);

		// 2-1. (사용자) 정산내역 삽입
		String settlementNo = partyAfterMapper.getPartySettlementNo(partyNo);
		SettlementDetail settlementDetail = SettlementDetail.builder()
															.partySettleNo(settlementNo)
															.pointHistoryNo(memberHistoryPk)
															.partySettlePrice(pointChange)
															.build();
		partyAfterMapper.insertSettlementDetail(settlementDetail);

		// 2-2. (사용자) 포인트 증가
		pointMapper.update(memberPointNo, pointChange);

		// 3. (관리자, 사용자) 포인트 내역 업데이트(성공)
		pointHistoryMapper.updateHistorySuccess(adminHistoryPk, adminCurrentPoint - pointChange);
		pointHistoryMapper.updateHistorySuccess(memberHistoryPk, memberCurrentPoint + pointChange);

		// 4-1. 정산현황 업데이트
		partyAfterMapper.updateSettlementState(settlementNo, STATUS_SETTLEMENT_COMPLETE.getCode());

		// 4-1-1. 정산완료 알림 전송(파티장)
		AlertSSE alert = new AlertSSE();
		alert.setMemberNo(memberNo);
		alert.setTypeCode(party.getCode());
		alert.setAlertDetail(MESSAGE_ALERT_PARTY_SETTLEMENT_COMPLETE.toString());
		alertSSEMapper.insertAlertSSE(alert);

		// 4-2. 파티 상태 변경(파티완료)
		partyAfterMapper.updatePartyComplete(partyNo);

		// TODO: 4-3. 게시글 상태 변경(파티완료)

	}

}
