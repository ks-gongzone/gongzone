package com.gongzone.central.point.service;

import com.gongzone.central.point.domain.PointChange;
import com.gongzone.central.point.domain.PointHistory;
import java.util.List;
import java.util.Map;

public interface PointService {
	Map<String, List<PointHistory>> getAllHistory(String memberPointNo);

	Map<String, Integer> getCurrentPoint(String memberPointNo);

	Map<String, String> updateMemberPoint(String memberPointNo, PointChange request);

}
