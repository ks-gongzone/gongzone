package com.gongzone.central.common.pagination;

import java.util.List;
import lombok.Data;

@Data
public class Pagination<T> {

	private int current;
	private int max;
	private List<T> elements;

}
