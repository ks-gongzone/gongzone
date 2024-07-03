package com.gongzone.central.point.payment.mapper;

import com.gongzone.central.point.payment.domain.Payment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {
	void insertPaymentHistory(Payment payment);

}
