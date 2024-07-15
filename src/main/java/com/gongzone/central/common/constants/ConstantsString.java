package com.gongzone.central.common.constants;

public enum ConstantsString {
	ADMIN_POINT_NO("MP000001", "어드민 포인트 번호");

	private String value;
	private String description;

	ConstantsString(String value, String description) {
		this.value = value;
		this.description = description;
	}

	@Override
	public String toString() {
		return this.value;
	}
}
