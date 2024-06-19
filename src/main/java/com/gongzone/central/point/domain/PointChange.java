package com.gongzone.central.point.domain;

import lombok.Data;

@Data
public class PointChange {
	private int pointBefore;
	private int pointChange;
	private int pointAfter;
	private String changeType;
	private String changeStatus;

}
