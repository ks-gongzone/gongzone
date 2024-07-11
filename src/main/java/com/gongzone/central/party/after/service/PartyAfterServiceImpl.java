package com.gongzone.central.party.after.service;

import static com.gongzone.central.utils.TypeCode.TYPE_POINT_DECREASE_PAYMENT;

import com.gongzone.central.party.after.domain.PartyPurchaseDetail;
import com.gongzone.central.party.after.domain.request.PartyPurchaseRequest;
import com.gongzone.central.party.after.mapper.PartyAfterMapper;
import com.gongzone.central.point.domain.request.PointDecreaseRequest;
import com.gongzone.central.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}
