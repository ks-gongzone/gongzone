package com.gongzone.central.point.domain.request;

import com.gongzone.central.point.withdrawal.domain.Withdraw;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class PointWithdrawRequest extends PointRequest {
	
	private Withdraw withdraw;

}
