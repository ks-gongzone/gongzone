package com.gongzone.central.point.domain.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PointDTO<T> {

	private int pointBefore;
	private int pointChange;
	private int pointAfter;
	private String changeType;
	private T detail;

}
