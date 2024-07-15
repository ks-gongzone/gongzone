package com.gongzone.central.point.service;

import com.gongzone.central.point.domain.PointHistory;
import com.gongzone.central.point.domain.request.PointRequest;
import java.util.List;

public interface PointHistoryService {

	String insert(String memberPointNo, PointRequest request);

	void calculatePointUpdate(String memberPointNo, PointRequest request);

	void updateSuccess(String historyNo, PointRequest pointCharge);

	List<PointHistory> getHistories(String memberNo, int size, int page);

	PointHistory getHistory(String memberNo, String pointHistoryNo);

	String insertHistory(String memberNo, PointRequest request);

	void updateHistorySuccess(String historyNo, PointRequest request);

}
