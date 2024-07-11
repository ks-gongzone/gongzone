package com.gongzone.central.party.after.service;

import static com.gongzone.central.utils.StatusCode.STATUS_BOARD_RECRUIT_COMPLETE;
import static com.gongzone.central.utils.StatusCode.STATUS_PARTY_PAYMENT_WAITING_MEMBER;
import static com.gongzone.central.utils.TypeCode.TYPE_POINT_DECREASE_PAYMENT;

import com.gongzone.central.party.after.domain.PartyPurchaseDetail;
import com.gongzone.central.party.after.domain.request.PartyPurchaseRequest;
import com.gongzone.central.party.after.mapper.PartyAfterMapper;
import com.gongzone.central.point.domain.request.PointDecreaseRequest;
import com.gongzone.central.point.service.PointService;
import com.gongzone.central.utils.MySqlUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartyAfterServiceImpl implements PartyAfterService {

	private final PartyAfterMapper partyAfterMapper;
	private final PointService pointService;


	@Override
	public void purchase(PartyPurchaseRequest partyPurchaseRequest) {
		PartyPurchaseDetail partyPurchaseDetail = partyPurchaseRequest.getPartyPurchaseDetail();
		// 1. 포인트 삭감 객체 생성한다.
		PointDecreaseRequest request = PointDecreaseRequest.builder()
														   .amount(partyPurchaseDetail.getPurchasePrice())
														   .changeType(TYPE_POINT_DECREASE_PAYMENT.getCode())
														   .build();

		try {
			// 2. 포인트 삭감한다.
			String historyNo = pointService.decrease(partyPurchaseRequest.getMemberPointNo(), request);

			// 3. 상세정보(PartyPurchaseDetail) 삽입
			partyPurchaseDetail.setPointHistoryNo(historyNo);
			partyAfterMapper.insertPurchaseDetail(partyPurchaseDetail);

			// 4. 결제내역(PartyPurchase) 업데이트
			String purchaseNo = partyPurchaseDetail.getPurchaseNo();
			partyAfterMapper.updatePurchaseSuccess(purchaseNo);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 테스트: 활성 파티 삽입
	 */
	@Override
	@Transactional
	public void testInsertParty() {
		// 게시글 삽입
		String boardNo = MySqlUtil.generatePrimaryKey(partyAfterMapper.testGetLastIdxBoard());
		partyAfterMapper.testInsertBoard(boardNo);
		// 위치 삽입
		partyAfterMapper.testInsertLocation(boardNo);
		// 사진 삽입
		int fileNo = partyAfterMapper.testInsertFile(boardNo);
		partyAfterMapper.testInsertFileRelation(fileNo, boardNo);
		// 파티 삽입
		String partyNo = MySqlUtil.generatePrimaryKey(partyAfterMapper.testGetLastIdxParty());
		partyAfterMapper.testInsertParty(partyNo, boardNo);
		// 파티장 삽입
		String purchasePrice1 = "1000";
		String partyMemberNo1 = MySqlUtil.generatePrimaryKey(partyAfterMapper.testGetLastIdxPartyMember());
		partyAfterMapper.testInsertPartyLeader(partyMemberNo1, partyNo, purchasePrice1);
		// 파티원 삽입
		String purchasePrice2 = "1000";
		String partyMemberNo2 = MySqlUtil.generatePrimaryKey(partyAfterMapper.testGetLastIdxPartyMember());
		partyAfterMapper.testInsertPartyMember(partyMemberNo2, partyNo, purchasePrice2);
		// 게시글(모집완료), 파티(파티원 결제대기) 상태변경
		partyAfterMapper.testChangeBoardStatus(boardNo, STATUS_BOARD_RECRUIT_COMPLETE.getCode());
		partyAfterMapper.testChangePartyStatus(partyNo, STATUS_PARTY_PAYMENT_WAITING_MEMBER.getCode());
		// 파티장, 파티원 결제현황 삽입
		partyAfterMapper.testInsertPartyPurchase(partyNo, partyMemberNo1, purchasePrice1);
		partyAfterMapper.testInsertPartyPurchase(partyNo, partyMemberNo2, purchasePrice2);
	}

}
