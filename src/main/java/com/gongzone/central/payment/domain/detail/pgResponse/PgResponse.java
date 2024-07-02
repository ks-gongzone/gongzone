package com.gongzone.central.payment.domain.detail.pgResponse;

import lombok.Data;

@Data
public class PgResponse {
	private String mId;
	private String lastTransactionKey;
	private String paymentKey;
	private String orderId;
	private String orderName;
	private int taxExemptionAmount;
	private String status;
	private String requestedAt;
	private String approvedAt;
	private boolean useEscrow;
	private boolean cultureExpense;
	private PgResponseCard card;
	private String virtualAccount;
	private String transfer;
	private String mobilePhone;
	private String giftCertificate;
	private String cashReceipt;
	private String cashReceipts;
	private String discount;
	private String cancels;
	private String secret;
	private String type;
	private PgResponseEasyPay easyPay;
	private String country;
	private String failure;
	private boolean isPartialCancelable;
	private PgResponseReceipt receipt;
	private PgResponseCheckout checkout;
	private String transactionKey;
	private String currency;
	private int totalAmount;
	private int balanceAmount;
	private int suppliedAmount;
	private int vat;
	private int taxFreeAmount;
	private String method;
	private String version;

}
