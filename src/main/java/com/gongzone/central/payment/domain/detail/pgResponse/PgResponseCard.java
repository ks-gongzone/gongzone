package com.gongzone.central.payment.domain.detail.pgResponse;

import lombok.Data;

@Data
public class PgResponseCard {
	private String company;
	private String issuerCode;
	private String acquirerCode;
	private String number;
	private int installmentPlanMonths;
	private boolean isInterestFree;
	private String interestPayer;
	private String approveNo;
	private boolean useCardPoint;
	private String cardType;
	private String ownerType;
	private String acquireStatus;
	private String receiptUrl;
	private int amount;

}
