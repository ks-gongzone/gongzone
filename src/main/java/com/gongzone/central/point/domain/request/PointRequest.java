package com.gongzone.central.point.domain.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class PointRequest {

	private int pointBefore;
	private int pointChange;
	private int pointAfter;
	private String changeType;

}
