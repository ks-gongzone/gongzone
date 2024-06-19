package com.gongzone.central.point.service;

import com.gongzone.central.point.domain.PointCharge;
import com.gongzone.central.point.domain.PointHistory;
import java.util.List;
import java.util.Map;

public interface PointService {
	Map<String, List<PointHistory>> getAllHistory(String memberPointNo);

	Map<String, Integer> getCurrentPoint(String memberPointNo);

	Map<String, String> chargeMemberPoint(String memberPointNo, PointCharge request);

	void insertPointHistory(PointHistory pointHistory);

}
