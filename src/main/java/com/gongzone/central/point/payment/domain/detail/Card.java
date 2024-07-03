package com.gongzone.central.point.payment.domain.detail;

import lombok.Data;

@Data
public class Card {
	private String publisher;
	private String issuer;
	private String brand;
	private String type;
	private String ownerType;
	private String bin;
	private String name;
	private String number;

}
