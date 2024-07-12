package com.gongzone.central.party.after.service;

import static com.gongzone.central.utils.StatusCode.STATUS_BOARD_RECRUIT_COMPLETE;
import static com.gongzone.central.utils.StatusCode.STATUS_PARTY_PAYMENT_WAITING_LEADER;
import static com.gongzone.central.utils.StatusCode.STATUS_PARTY_PAYMENT_WAITING_MEMBER;

import com.gongzone.central.party.after.domain.PartyPurchaseDetail;
import com.gongzone.central.party.after.mapper.PartyAfterMapper;
import com.gongzone.central.point.domain.request.PointRequest;
import com.gongzone.central.point.service.PointService;
import com.gongzone.central.utils.MySqlUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartyAfterServiceImpl implements PartyAfterService {

	private final PointService pointService;

	private final PartyAfterMapper partyAfterMapper;


	@Override
	@Transactional
	public void purchase(String partyNo, String memberNo, PointRequest request) {
		// 1. 포인트 내역 삽입한다.
		String historyNo = pointService.insertHistory(memberNo, request);

		// 1-1. 결제 내역 상세 생성
		// TODO: 보유 포인트 확인
		PartyPurchaseDetail partyPurchaseDetail = (PartyPurchaseDetail) request.getDetail();
		partyPurchaseDetail.setPointHistoryNo(historyNo);

		// 보유 포인트 유효할 시
		// 2. 결제 상세정보(PartyPurchaseDetail) 삽입
		partyAfterMapper.insertPurchaseDetail(partyPurchaseDetail);

		// 2-1. 결제 내역 업데이트(완료)
		String purchaseNo = partyPurchaseDetail.getPurchaseNo();
		partyAfterMapper.updatePurchaseComplete(purchaseNo);

		// 3. 포인트 삭감한다.
		pointService.update(memberNo, request);

		// 4. 포인트 내역 업데이트(성공)
		pointService.updateHistorySuccess(historyNo, request);

		// 5. 파티 결제현황 확인
		if (partyAfterMapper.checkPurchaseComplete(partyNo)) {
			// 5-1. 파티 상태 업데이트(파티장 결제대기)
			partyAfterMapper.testChangePartyStatus(partyNo, STATUS_PARTY_PAYMENT_WAITING_LEADER.getCode());
			// TODO: 파티장에게 결제 요청 알림 발송
			// NOTE: 결제 요청 알림과 함께 쇼핑몰 배송이 출발하면 운송장 번호 입력 요청

			// 5-2. 파티 배송 현황 삽입
			partyAfterMapper.insertPartyShipping(partyNo);
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
		// 파티원 삽입
		String purchasePrice = "1000";
		String partyMemberNo = MySqlUtil.generatePrimaryKey(partyAfterMapper.testGetLastIdxPartyMember());
		partyAfterMapper.testInsertPartyMember(partyMemberNo, partyNo, purchasePrice);
		// 게시글(모집완료), 파티(파티원 결제대기) 상태변경
		partyAfterMapper.testChangeBoardStatus(boardNo, STATUS_BOARD_RECRUIT_COMPLETE.getCode());
		partyAfterMapper.testChangePartyStatus(partyNo, STATUS_PARTY_PAYMENT_WAITING_MEMBER.getCode());
		// 파티원 결제현황 삽입
		partyAfterMapper.testInsertPartyPurchase(partyNo, partyMemberNo, purchasePrice);
	}

}
