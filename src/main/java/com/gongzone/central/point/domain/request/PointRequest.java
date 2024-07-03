package com.gongzone.central.point.domain.request;

import lombok.Data;

@Data
public class PointRequest {
	private int pointBefore;
	private int pointChange;
	private int pointAfter;
	private String changeType;

}
