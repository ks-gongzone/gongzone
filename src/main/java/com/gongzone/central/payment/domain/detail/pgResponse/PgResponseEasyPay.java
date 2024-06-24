package com.gongzone.central.payment.domain.detail.pgResponse;

import lombok.Data;

@Data
public class PgResponseEasyPay {
	private String provider;
	private int amount;
	private int discountAmount;

}
