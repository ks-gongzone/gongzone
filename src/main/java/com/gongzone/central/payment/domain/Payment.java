package com.gongzone.central.payment.domain;

import lombok.Data;

@Data
public class Payment {
	private String transactionType;
	private String txId;
	private String paymentId;
	private String code;
	private String message;

}
