package com.gongzone.central.point.service.impl;

import com.gongzone.central.point.domain.request.PointRequest;
import com.gongzone.central.point.mapper.PointMapper;
import com.gongzone.central.point.payment.domain.Payment;
import com.gongzone.central.point.payment.service.PaymentHistoryService;
import com.gongzone.central.point.service.PointHistoryService;
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
	public void charge(String memberPointNo, PointRequest request) {
		// 1. 포인트 내역 삽입
		String historyNo = pointHistoryService.insert(memberPointNo, request);

		// 1-1. 충전 내역 생성
		// TODO: 포트원 서버에 충전 정보 확인
		Payment payment = (Payment) request.getDetail();
		payment.setPointHistoryNo(historyNo);

		// 충전 정보 유효할 시
		// 2. 충전 내역 삽입
		paymentHistoryService.insert(payment);

		// 3. 포인트 증가
		updatePoint(memberPointNo, request);

		// 4. 포인트 내역 업데이트(성공)
		pointHistoryService.updateSuccess(historyNo, request);
	}

	/**
	 * 요청을 기반으로 회원 포인트 인출을 처리한다.
	 *
	 * @param memberPointNo 회원 포인트 번호
	 * @param request       회원 포인트 인출 객체
	 */
	public void withdraw(String memberPointNo, PointRequest request) {
		// 1. 포인트 내역 삽입
		String historyNo = pointHistoryService.insert(memberPointNo, request);

		// TODO: 실제 운영 계좌에서 포인트 출금 처리
		// 1-1. 인출 내역 생성
		Withdraw withdraw = (Withdraw) request.getDetail();
		withdraw.setPointHistoryNo(historyNo);

		// 2. 인출 내역 삽입
		withdrawHistoryService.insert(withdraw);
		// TODO: 관리자 인출 처리 필요(관리자 페이지에 구현)
		// NOTE: 인출 상태코드 테이블 변경 필요 -> (인출 대기중, 인출 완료)

		// 3. 포인트 차감
		updatePoint(memberPointNo, request);

		// 4. 포인트 내역 업데이트(성공)
		pointHistoryService.updateSuccess(historyNo, request);
	}

	/**
	 * 포인트 변동 객체를 이용해 실제로 포인트를 변화시킨다.
	 *
	 * @param memberPointNo 회원 포인트 번호
	 * @param request       포인트 변동 객체
	 */
	public void updatePoint(String memberPointNo, PointRequest request) {
		int change = request.getPointChange();
		pointMapper.updatePoint(memberPointNo, change);
	}

}
