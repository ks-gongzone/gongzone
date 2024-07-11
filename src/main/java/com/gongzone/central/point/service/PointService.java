package com.gongzone.central.point.service;

import com.gongzone.central.point.domain.PointHistory;
import com.gongzone.central.point.domain.request.PointChargeRequest;
import com.gongzone.central.point.domain.request.PointWithdrawRequest;
import java.util.List;

public interface PointService {

	List<PointHistory> getHistories(String memberNo, int size, int page);

	PointHistory getHistory(String memberNo, String pointHistoryNo);

	Integer getCurrentPoint(String memberNo);

	void charge(String memberNo, PointChargeRequest request);

	void withdraw(String memberNo, PointWithdrawRequest request);

}
