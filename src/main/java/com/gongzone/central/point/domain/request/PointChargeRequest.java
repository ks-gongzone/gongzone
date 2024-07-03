package com.gongzone.central.point.domain.request;

import com.gongzone.central.point.payment.domain.Payment;
import lombok.Data;

@Data
public class PointChargeRequest extends PointRequest {
	private Payment payment;

}
