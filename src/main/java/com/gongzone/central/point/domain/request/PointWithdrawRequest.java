package com.gongzone.central.point.domain.request;

import com.gongzone.central.point.withdrawal.domain.Withdraw;
import lombok.Data;

@Data
public class PointWithdrawRequest extends PointRequest {
	private Withdraw withdraw;

}
