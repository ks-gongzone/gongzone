package com.gongzone.central.payment.domain.detail;

import lombok.Data;

@Data
public class Method {
	private String type;
	private String provider;
	private EasyPayMethod easyPayMethod;

}
