package com.gongzone.central.payment.domain.detail;

import com.gongzone.central.payment.domain.detail.pgResponse.PgResponse;
import lombok.Data;

@Data
public class PaymentInfo {
	private String status;
	private String id;
	private String transactionId;
	private String merchantId;
	private String storeId;
	private Method method;
	private Channel channel;
	private String version;
	private String requestedAt;
	private String updatedAt;
	private String statusChangedAt;
	private String orderName;
	private Amount amount;
	private String currency;
	private Customer customer;
	private boolean isCulturalExpense;
	private String paidAt;
	private String pgTxId;
	private PgResponse pgResponse;
	private String receiptUrl;

}
