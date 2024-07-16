package com.gongzone.central.point.payment.service;

import com.gongzone.central.point.payment.domain.Payment;
import com.gongzone.central.point.payment.domain.detail.PaymentInfo;
import java.util.List;

public interface PaymentService {

	void insert(Payment payment);

	Payment get(String paymentNo);

	PaymentInfo getDetail(String paymentNo);

	List<Payment> getMany(String memberNo, int size, int page);

}
