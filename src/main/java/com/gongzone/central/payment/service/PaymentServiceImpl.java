package com.gongzone.central.payment.service;

import com.gongzone.central.payment.api.IamportClient;
import com.gongzone.central.payment.domain.Payment;
import com.gongzone.central.payment.domain.detail.PaymentInfo;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
	private final IamportClient iamportClient;

	@Override
	public Map<String, PaymentInfo> requestPayment(Payment paymentDetail) {
		Map<String, PaymentInfo> result = new HashMap<>();

		String paymentId = paymentDetail.getPaymentId();
		PaymentInfo payInfo = iamportClient.getPaymentInfo(paymentId).block();

		result.put("result", payInfo);

		return result;
	}

}
