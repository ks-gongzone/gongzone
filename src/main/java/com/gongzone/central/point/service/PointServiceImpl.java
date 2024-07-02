package com.gongzone.central.point.service;

import static com.gongzone.central.utils.StatusCode.STATUS_POINT_WITHDRAW_1;

import com.gongzone.central.point.payment.domain.Payment;
import com.gongzone.central.point.payment.service.PaymentService;
import com.gongzone.central.point.domain.PointHistory;
import com.gongzone.central.point.domain.request.PointChargeRequest;
import com.gongzone.central.point.domain.request.PointWithdrawRequest;
import com.gongzone.central.point.mapper.PointMapper;
import com.gongzone.central.point.withdrawal.domain.Withdraw;
import com.gongzone.central.point.withdrawal.service.WithdrawalService;
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
	private final WithdrawalService withdrawalService;


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
	 * 요청에 따라 회원 포인트를 충전한다.
	 *
	 * @param memberPointNo 회원 포인트 번호
	 * @param request       포인트 충전 객체
	 * @return 결과
	 */
	@Override
	public Map<String, String> chargeMemberPoint(String memberPointNo, PointChargeRequest request) {
		Map<String, String> response = new HashMap<>();
		pointTransactionService.calculatePointUpdate(memberPointNo, request);

		String historyNo = pointHistoryService.insertPointHistory(memberPointNo, request);

		// TODO: 결제 정보 확인
		Payment payment = request.getPayment();
		payment.setPointHistoryNo(historyNo);
		paymentService.insertPaymentHistory(payment);

		// 포인트 충전 성공 시, 보유 포인트 update 및 충전 내역 update
		pointTransactionService.updatePoint(memberPointNo, request);
		pointHistoryService.updateHistorySuccess(historyNo, request);

		return response;
	}

	@Override
	public Map<String, String> withdrawMemberPoint(String memberPointNo, PointWithdrawRequest request) {
		Map<String, String> response = new HashMap<>();
		pointTransactionService.calculatePointUpdate(memberPointNo, request);

		request.setPointChange(-request.getPointChange());
		String historyNo = pointHistoryService.insertPointHistory(memberPointNo, request);

		// TODO: 실제 운영 계좌에서 포인트 출금 처리
		Withdraw withdraw = request.getWithdraw();
		withdraw.setPointHistoryNo(historyNo);

		// 포인트 인출 성공 시
		withdraw.setStatusCode(STATUS_POINT_WITHDRAW_1.getCode());
		withdrawalService.insertPointWithdraw(withdraw);
		System.out.println();
		System.out.println(withdraw);
		System.out.println();

		pointTransactionService.updatePoint(memberPointNo, request);
		pointHistoryService.updateHistorySuccess(historyNo, request);

		return response;
	}

}
