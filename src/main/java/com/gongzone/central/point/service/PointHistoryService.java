package com.gongzone.central.point.service;

import com.gongzone.central.common.pagination.Pagination;
import com.gongzone.central.point.domain.PointHistory;
import com.gongzone.central.point.domain.request.PointDTO;

public interface PointHistoryService {

	String insert(String memberPointNo, PointDTO request);

	PointHistory get(String pointHistoryNo);

	Pagination getMany(String memberNo, int size, int page);

	void updateSuccess(String historyNo, PointDTO request);

}
