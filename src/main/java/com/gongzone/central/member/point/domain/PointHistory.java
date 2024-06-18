package com.gongzone.central.member.point.domain;

import com.gongzone.central.utils.StatusCode;
import com.gongzone.central.utils.TypeCode;
import lombok.Data;

@Data
public class PointHistory {
	private String pointHistoryNo;
	private String memberPointNo;
	private String type;
	private String pointHistoryBefore;
	private String pointHistoryChange;
	private String pointHistoryAfter;
	private String pointHistoryDate;
	private String status;

	public void setType(String type) {
		this.type = TypeCode.getDescriptionByCode(type);
	}

	public void setStatus(String status) {
		this.status = StatusCode.getDescriptionByCode(status);
	}

}
