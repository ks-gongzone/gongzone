package com.gongzone.central.point.payment.service;

import com.gongzone.central.point.mapper.PointMapper;
import com.gongzone.central.point.payment.api.PortOneClient;
import com.gongzone.central.point.payment.domain.Payment;
import com.gongzone.central.point.payment.domain.detail.PaymentInfo;
import com.gongzone.central.point.payment.mapper.PaymentMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final PortOneClient portOneClient;

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
		String memberPointNo = pointMapper.getPointNo(memberNo);
		return paymentMapper.getMany(memberPointNo, size, page - 1);
	}

}
