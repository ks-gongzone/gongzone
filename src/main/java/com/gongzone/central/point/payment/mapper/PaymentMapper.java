package com.gongzone.central.point.payment.mapper;

import com.gongzone.central.point.payment.domain.Payment;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PaymentMapper {

	void insert(Payment payment);

	Payment get(String paymentNo);

	List<Payment> getMany(@Param("memberPointNo") String memberPointNo,
						  @Param("size") int size,
						  @Param("page") int page);

}
