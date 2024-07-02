package com.gongzone.central.payment.domain.detail;

import lombok.Data;

@Data
public class Installment {
	private int month;
	private boolean isInterestFree;

}
