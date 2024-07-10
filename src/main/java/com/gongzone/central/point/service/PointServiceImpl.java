package com.gongzone.central.point.service;


import static com.gongzone.central.utils.StatusCode.STATUS_POINT_WITHDRAW_SUCCESS;

import com.gongzone.central.point.domain.PointHistory;
import com.gongzone.central.point.domain.request.PointChargeRequest;
import com.gongzone.central.point.domain.request.PointWithdrawRequest;
import com.gongzone.central.point.mapper.PointMapper;
import com.gongzone.central.point.payment.domain.Payment;
import com.gongzone.central.point.payment.service.PaymentHistoryService;
import com.gongzone.central.point.withdrawal.domain.Withdraw;
import com.gongzone.central.point.withdrawal.service.WithdrawHistoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
	private final PointTransactionService pointTransactionService;
	private final PointHistoryService pointHistoryService;
	private final PaymentHistoryService paymentHistoryService;
	private final WithdrawHistoryService withdrawHistoryService;

	private final PointMapper pointMapper;


	/**
	 * 회원의 (모든) 포인트 사용 내역을 반환한다.
	 *
	 * @param memberPointNo 회원 포인트 번호
	 * @param size          페이지 크기
	 * @param page          페이지 번호
	 * @return 포인트 사용내역 List
	 */
	@Override
	public List<PointHistory> getHistories(String memberPointNo, int size, int page) {
		return pointMapper.getHistories(memberPointNo, size, page - 1);
	}

	/**
	 * 회원의 포인트 사용 내역을 반환한다.
	 *
	 * @param memberPointNo  회원 포인트 번호
	 * @param pointHistoryNo 포인트 내역 번호
	 * @return 포인트 사용내역
	 */
	@Override
	public PointHistory getHistory(String memberPointNo, String pointHistoryNo) {
		return pointMapper.getHistory(memberPointNo, pointHistoryNo);
	}

	/**
	 * 회원이 현재 보유한 포인트를 반환한다.
	 *
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
	public void charge(String memberPointNo, PointChargeRequest request) {
		String historyNo = pointHistoryService.insert(memberPointNo, request);

		// TODO: 결제 정보 확인
		Payment payment = request.getPayment();
		payment.setPointHistoryNo(historyNo);

		// 포인트 충전 성공 시, 보유 포인트 update 및 충전 내역 update
		paymentHistoryService.insert(payment);

		pointTransactionService.updatePoint(memberPointNo, request);
		pointHistoryService.updateSuccess(historyNo, request);
	}

	/**
	 * 요청을 기반으로 회원 포인트 인출을 처리한다.
	 *
	 * @param memberPointNo 회원 포인트 번호
	 * @param request       회원 포인트 인출 객체
	 */
	@Override
	public void withdraw(String memberPointNo, PointWithdrawRequest request) {
		String historyNo = pointHistoryService.insert(memberPointNo, request);

		// TODO: 실제 운영 계좌에서 포인트 출금 처리
		Withdraw withdraw = request.getWithdraw();
		withdraw.setPointHistoryNo(historyNo);

		// 포인트 인출 성공 시
		withdraw.setStatusCode(STATUS_POINT_WITHDRAW_SUCCESS.getCode());
		withdrawHistoryService.insert(withdraw);

		pointTransactionService.updatePoint(memberPointNo, request);
		pointHistoryService.updateSuccess(historyNo, request);
	}

}
