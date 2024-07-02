package com.gongzone.central.point.service;

import static com.gongzone.central.utils.StatusCode.STATUS_POINT_WITHDRAW_1;

import com.gongzone.central.point.domain.PointHistory;
import com.gongzone.central.point.domain.request.PointChargeRequest;
import com.gongzone.central.point.domain.request.PointWithdrawRequest;
import com.gongzone.central.point.mapper.PointMapper;
import com.gongzone.central.point.payment.domain.Payment;
import com.gongzone.central.point.payment.service.PaymentService;
import com.gongzone.central.point.withdrawal.domain.Withdraw;
import com.gongzone.central.point.withdrawal.service.WithdrawalService;
import java.util.List;
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


	/**
	 * @param memberPointNo 회원 포인트 번호
	 * @return 포인트 사용내역
	 */
	@Override
	public List<PointHistory> getAllHistory(String memberPointNo) {
		return pointMapper.getAllHistory(memberPointNo);
	}

	/**
	 * @param memberPointNo 회원 포인트 번호
	 * @return 현재 보유 포인트
	 */
	@Override
	public Integer getCurrentPoint(String memberPointNo) {
		return pointMapper.getCurrentPoint(memberPointNo);
	}

	/**
	 * 요청을 기반으로 회원 포인트 충전을 처리한다.
	 *
	 * @param memberPointNo 회원 포인트 번호
	 * @param request       포인트 충전 객체
	 */
	@Override
	public void chargeMemberPoint(String memberPointNo, PointChargeRequest request) {
		String historyNo = pointHistoryService.insertPointHistory(memberPointNo, request);

		// TODO: 결제 정보 확인
		Payment payment = request.getPayment();
		payment.setPointHistoryNo(historyNo);

		// 포인트 충전 성공 시, 보유 포인트 update 및 충전 내역 update
		paymentService.insertPaymentHistory(payment);

		pointTransactionService.updatePoint(memberPointNo, request);
		pointHistoryService.updateHistorySuccess(historyNo, request);
	}

	/**
	 * 요청을 기반으로 회원 포인트 인출을 처리한다.
	 *
	 * @param memberPointNo 회원 포인트 번호
	 * @param request       회원 포인트 인출 객체
	 */
	@Override
	public void withdrawMemberPoint(String memberPointNo, PointWithdrawRequest request) {
		String historyNo = pointHistoryService.insertPointHistory(memberPointNo, request);

		// TODO: 실제 운영 계좌에서 포인트 출금 처리
		Withdraw withdraw = request.getWithdraw();
		withdraw.setPointHistoryNo(historyNo);

		// 포인트 인출 성공 시
		withdraw.setStatusCode(STATUS_POINT_WITHDRAW_1.getCode());
		withdrawalService.insertPointWithdraw(withdraw);

		pointTransactionService.updatePoint(memberPointNo, request);
		pointHistoryService.updateHistorySuccess(historyNo, request);
	}

}
