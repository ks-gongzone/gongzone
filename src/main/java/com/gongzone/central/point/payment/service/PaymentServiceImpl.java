package com.gongzone.central.point.payment.service;

import com.gongzone.central.point.domain.request.PointDTO;
import com.gongzone.central.point.mapper.PointMapper;
import com.gongzone.central.point.payment.api.PortOneClient;
import com.gongzone.central.point.payment.domain.Payment;
import com.gongzone.central.point.payment.domain.detail.PaymentInfo;
import com.gongzone.central.point.payment.mapper.PaymentMapper;
import com.gongzone.central.point.service.PointHistoryService;
import com.gongzone.central.point.service.PointService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final PortOneClient portOneClient;
	private final PointService pointService;
	private final PointHistoryService pointHistoryService;

	private final PointMapper pointMapper;
	private final PaymentMapper paymentMapper;

	@Override
	public void insert(Payment payment) {
		// TODO: 요청받은 금액과 포트원 서버 상의 금액이 일치하는지 확인
		// TODO: 결제 정보가 다르다면 예외 발생

		paymentMapper.insert(payment);
	}

	@Override
	public Payment get(String paymentNo) {
		return paymentMapper.get(paymentNo);
	}

	@Override
	public PaymentInfo getDetail(String paymentNo) {
		String paymentId = get(paymentNo).getPaymentId();
		return portOneClient.getPaymentInfo(paymentId).block();
	}

	@Override
	public List<Payment> getMany(String memberNo, int size, int page) {
		String memberPointNo = pointMapper.getMemberPointNo(memberNo);
		return paymentMapper.getMany(memberPointNo, size, page - 1);
	}

	/**
	 * 요청을 기반으로 회원 포인트 충전을 처리한다.
	 *
	 * @param memberNo 회원 번호
	 * @param request  포인트 충전 객체
	 */
	@Override
	public void charge(String memberNo, PointDTO request) {
		String memberPointNo = pointService.getMemberPointNo(memberNo);

		// 1. 포인트 내역 삽입
		String historyNo = pointHistoryService.insert(memberPointNo, request);

		// 1-1. 충전 내역 생성
		// TODO: 포트원 서버에 충전 정보 확인
		Payment payment = (Payment) request.getDetail();
		payment.setPointHistoryNo(historyNo);

		// 충전 정보 유효할 시
		// 2. 충전 내역 삽입
		insert(payment);

		// 3. 포인트 증가
		pointService.updatePoint(memberPointNo, request);

		// 4. 포인트 내역 업데이트(성공)
		pointHistoryService.updateSuccess(historyNo, request);

	}

}
