package com.gongzone.central.point.payment.service;

import com.gongzone.central.point.payment.api.IamportClient;
import com.gongzone.central.point.payment.domain.Payment;
import com.gongzone.central.point.payment.domain.detail.PaymentInfo;
import com.gongzone.central.point.payment.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentHistoryServiceImpl implements PaymentHistoryService {
	private final IamportClient iamportClient;
	private final PaymentMapper paymentMapper;

	@Override
	public void insert(Payment payment) {
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
