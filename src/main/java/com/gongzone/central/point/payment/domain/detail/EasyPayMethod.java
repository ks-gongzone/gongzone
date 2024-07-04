package com.gongzone.central.point.payment.domain.detail;

import lombok.Data;

@Data
public class EasyPayMethod {
	private String type;
	private Card card;
	private String approvalNumber;
	private Installment installment;
	private boolean pointUsed;

}
