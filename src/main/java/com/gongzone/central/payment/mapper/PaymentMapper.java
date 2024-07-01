package com.gongzone.central.payment.mapper;

import com.gongzone.central.payment.domain.Payment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {
	void insertPaymentHistory(Payment payment);

}
