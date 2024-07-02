package com.gongzone.central.point.domain;

import com.gongzone.central.payment.domain.Payment;
import lombok.Data;

@Data
public class PointChangeRequest {
	private Payment payment;
	private int pointBefore;
	private int pointChange;
	private int pointAfter;
	private String changeType;

}
