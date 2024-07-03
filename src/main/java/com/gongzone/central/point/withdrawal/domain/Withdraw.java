package com.gongzone.central.point.withdrawal.domain;

import com.gongzone.central.utils.StatusCode;
import lombok.Data;

@Data
public class Withdraw {
	private String withdrawNo;
	private String pointHistoryNo;
	private String withdrawBank;
	private String withdrawAccount;
	private String withdrawName;
	private int withdrawAmount;
	private String withdrawDate;
	private String status;  // in: 코드, out: 상태값


	public void setStatusCode(String code) {
		// in: 코드
		this.status = code;
	}

	public void setStatus(String code) {
		// out: 상태값
		this.status = StatusCode.getDescriptionByCode(code);
	}

}
