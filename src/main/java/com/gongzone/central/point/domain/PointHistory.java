package com.gongzone.central.point.domain;

import com.gongzone.central.utils.StatusCode;
import com.gongzone.central.utils.TypeCode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PointHistory {
	private String pointHistoryNo;
	private String memberPointNo;
	private String type;
	private int pointHistoryBefore;
	private int pointHistoryChange;
	private int pointHistoryAfter;
	private String pointHistoryDate;
	private String status;

	public void setType(String code) {
		this.type = TypeCode.getDescriptionByCode(code);
	}

	public void setTypeCode(String code) {
		this.type = code;
	}

	public void setStatus(String code) {
		this.status = StatusCode.getDescriptionByCode(code);
	}

	public void setStatusCode(String code) {
		this.status = code;
	}

}
