package com.gongzone.central.point.domain.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class PointDecreaseRequest extends PointRequest {

	private String amount;

}
