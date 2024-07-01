package com.gongzone.central.payment.service;

import com.gongzone.central.payment.api.IamportClient;
import com.gongzone.central.payment.domain.Payment;
import com.gongzone.central.payment.domain.detail.PaymentInfo;
import com.gongzone.central.payment.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
	private final IamportClient iamportClient;
	private final PaymentMapper paymentMapper;

	@Override
	public void insertPaymentHistory(Payment payment) {
		// TODO: 요청받은 금액과 포트원 서버 상의 금액이 일치하는지 확인
		String paymentId = payment.getPaymentId();
		PaymentInfo payInfo = iamportClient.getPaymentInfo(paymentId).block();
		System.out.println();
		System.out.println(payInfo);
		System.out.println();

		// TODO: 결제 정보가 다르다면 예외 발생

		paymentMapper.insertPaymentHistory(payment);
	}

}
