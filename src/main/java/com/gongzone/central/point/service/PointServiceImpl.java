package com.gongzone.central.point.service;

import com.gongzone.central.payment.domain.Payment;
import com.gongzone.central.payment.service.PaymentService;
import com.gongzone.central.point.domain.PointChangeRequest;
import com.gongzone.central.point.domain.PointHistory;
import com.gongzone.central.point.mapper.PointMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
	private final PointTransactionService pointTransactionService;
	private final PointHistoryService pointHistoryService;
	private final PointMapper pointMapper;
	private final PaymentService paymentService;


	@Override
	public Map<String, List<PointHistory>> getAllHistory(String memberPointNo) {
		List<PointHistory> histories = pointMapper.getAllHistory(memberPointNo);
		Map<String, List<PointHistory>> result = new HashMap<>(
				Map.of("result", histories)
		);

		return result;
	}

	@Override
	public Map<String, Integer> getCurrentPoint(String memberPointNo) {
		Integer point = pointMapper.getCurrentPoint(memberPointNo);
		Map<String, Integer> result = new HashMap<>(
				Map.of("result", point)
		);

		return result;
	}

	/**
	 * 요청에 따라 회원 포인트를 증가/감소시킨다.
	 *
	 * @param memberPointNo 회원 포인트 번호
	 * @param pointChange   포인트 변동 객체
	 * @return 결과
	 */
	@Override
	public Map<String, String> updateMemberPoint(String memberPointNo, PointChangeRequest pointChange) {
		Map<String, String> response = new HashMap<>();
		pointTransactionService.calculatePointUpdate(memberPointNo, pointChange);

		String historyNo = pointHistoryService.insertPointHistory(memberPointNo, pointChange);

		// TODO: 결제 정보 확인
		Payment payment = pointChange.getPayment();
		payment.setPointHistoryNo(historyNo);
		paymentService.insertPaymentHistory(payment);

		// 포인트 충전 성공 시, 보유 포인트 update 및 충전 내역 update
		pointTransactionService.updatePoint(memberPointNo, pointChange);
		pointHistoryService.updateHistorySuccess(historyNo, pointChange);

		return response;
	}

}
