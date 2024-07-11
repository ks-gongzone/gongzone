package com.gongzone.central.point.service;

import static com.gongzone.central.utils.StatusCode.STATUS_POINT_WITHDRAW_SUCCESS;

import com.gongzone.central.point.domain.request.PointChargeRequest;
import com.gongzone.central.point.domain.request.PointRequest;
import com.gongzone.central.point.domain.request.PointWithdrawRequest;
import com.gongzone.central.point.mapper.PointMapper;
import com.gongzone.central.point.payment.domain.Payment;
import com.gongzone.central.point.payment.service.PaymentHistoryService;
import com.gongzone.central.point.withdrawal.domain.Withdraw;
import com.gongzone.central.point.withdrawal.service.WithdrawHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
@RequiredArgsConstructor
public class PointTransactionService {

	private final PointHistoryService pointHistoryService;
	private final PaymentHistoryService paymentHistoryService;
	private final WithdrawHistoryService withdrawHistoryService;

	private final PointMapper pointMapper;


	/**
	 * 요청을 기반으로 회원 포인트 충전을 처리한다.
	 *
	 * @param memberPointNo 회원 포인트 번호
	 * @param request       포인트 충전 객체
	 */
	public void charge(String memberPointNo, PointChargeRequest request) {
		String historyNo = pointHistoryService.insert(memberPointNo, request);

		// TODO: 결제 정보 확인
		Payment payment = request.getPayment();
		payment.setPointHistoryNo(historyNo);

		// 포인트 충전 성공 시, 보유 포인트 update 및 충전 내역 update
		paymentHistoryService.insert(payment);

		updatePoint(memberPointNo, request);
		pointHistoryService.updateSuccess(historyNo, request);
	}

	/**
	 * 요청을 기반으로 회원 포인트 인출을 처리한다.
	 *
	 * @param memberPointNo 회원 포인트 번호
	 * @param request       회원 포인트 인출 객체
	 */
	public void withdraw(String memberPointNo, PointWithdrawRequest request) {
		String historyNo = pointHistoryService.insert(memberPointNo, request);

		// TODO: 실제 운영 계좌에서 포인트 출금 처리
		Withdraw withdraw = request.getWithdraw();
		withdraw.setPointHistoryNo(historyNo);

		// 포인트 인출 성공 시
		withdraw.setStatusCode(STATUS_POINT_WITHDRAW_SUCCESS.getCode());
		withdrawHistoryService.insert(withdraw);

		updatePoint(memberPointNo, request);
		pointHistoryService.updateSuccess(historyNo, request);
	}

	/**
	 * 포인트 변동 객체를 이용해 실제로 포인트를 변화시킨다.
	 *
	 * @param memberPointNo 회원 포인트 번호
	 * @param request       포인트 변동 객체
	 */
	private void updatePoint(String memberPointNo, PointRequest request) {
		int change = request.getPointChange();
		pointMapper.updatePoint(memberPointNo, change);
	}

}
