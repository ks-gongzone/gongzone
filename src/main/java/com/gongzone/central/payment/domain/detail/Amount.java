package com.gongzone.central.payment.domain.detail;

import lombok.Data;

@Data
public class Amount {
	private int total;
	private int taxFree;
	private int vat;
	private int supply;
	private int discount;
	private int paid;
	private int cancelled;
	private int cancelledTaxFree;

}
