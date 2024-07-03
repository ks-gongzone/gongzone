package com.gongzone.central.point.service;

import com.gongzone.central.point.domain.PointHistory;
import com.gongzone.central.point.domain.request.PointChargeRequest;
import com.gongzone.central.point.domain.request.PointWithdrawRequest;
import java.util.List;
import java.util.Map;

public interface PointService {
	Map<String, List<PointHistory>> getAllHistory(String memberPointNo);

	Map<String, Integer> getCurrentPoint(String memberPointNo);

	Map<String, String> chargeMemberPoint(String memberPointNo, PointChargeRequest request);

	Map<String, String> withdrawMemberPoint(String memberPointNo, PointWithdrawRequest request);

}
