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

	/**
	 * 기본 setter, 마이바티스가 결과를 맵핑할 때 사용(상태코드 설명)
	 */
	public void setType(String code) {
		this.type = TypeCode.getDescriptionByCode(code);
	}

	public void setStatus(String code) {
		this.status = StatusCode.getDescriptionByCode(code);
	}

	
	/**
	 * 커스텀 setter, 마이바티스에 결과를 집어넣을 때 사용(상태코드 형식)
	 */
	public void setTypeCode(String code) {
		this.type = code;
	}

	public void setStatusCode(String code) {
		this.status = code;
	}

}
