package com.gongzone.central.point.payment.domain;

import lombok.Data;

@Data
public class Payment {
	private String paymentNo;
	private String pointHistoryNo;
	private String type;
	private String transactionType;  // PortOne
	private String txId;  // PortOne
	private String paymentId;  // PortOne
	private String code;  // PortOne
	private String message;  // PortOne
	private String status;

}
