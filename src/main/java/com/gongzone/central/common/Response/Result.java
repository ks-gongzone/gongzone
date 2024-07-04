package com.gongzone.central.common.Response;

import lombok.Data;

@Data
public class Result<T> {
	private final T result;

}
