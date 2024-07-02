package com.gongzone.central.point.payment.domain.detail;

import lombok.Data;

@Data
public class Channel {
	private String type;
	private String id;
	private String key;
	private String name;
	private String pgProvider;
	private String pgMerchantId;

}
