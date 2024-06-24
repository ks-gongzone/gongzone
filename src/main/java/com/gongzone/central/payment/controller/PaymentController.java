package com.gongzone.central.payment.controller;

import com.gongzone.central.payment.domain.Payment;
import com.gongzone.central.payment.domain.detail.PaymentInfo;
import com.gongzone.central.payment.service.PaymentService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
	private final PaymentService paymentService;

	@PostMapping("/complete")
	public Map<String, PaymentInfo> postPayment(@RequestBody Payment payment) {

		return paymentService.requestPayment(payment);
	}

}
