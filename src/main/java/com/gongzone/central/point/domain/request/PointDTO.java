package com.gongzone.central.point.domain.request;

import com.gongzone.central.utils.TypeCode;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PointDTO<T> {

	private int pointBefore;
	private int pointChange;
	private int pointAfter;
	private TypeCode changeType;
	private T detail;

}
