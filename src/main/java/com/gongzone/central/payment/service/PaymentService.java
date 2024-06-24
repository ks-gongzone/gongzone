package com.gongzone.central.payment.service;

import com.gongzone.central.payment.domain.Payment;
import com.gongzone.central.payment.domain.detail.PaymentInfo;
import java.util.Map;

public interface PaymentService {

	Map<String, PaymentInfo> requestPayment(Payment payment);

}
