package com.gongzone.central.party.after.domain;

import lombok.Data;

@Data
public class Shipping {

	private String shippingNo;
	private String partyNo;
	private String invoiceCourier;
	private String invoiceCode;
	private String addDate;
	private String statusCode;

}
