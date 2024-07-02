package com.gongzone.central.point.service;

import com.gongzone.central.point.domain.PointHistory;
import com.gongzone.central.point.domain.request.PointChargeRequest;
import com.gongzone.central.point.domain.request.PointWithdrawRequest;
import java.util.List;

public interface PointService {
	List<PointHistory> getAllHistory(String memberPointNo);

	Integer getCurrentPoint(String memberPointNo);

	void chargeMemberPoint(String memberPointNo, PointChargeRequest request);

	void withdrawMemberPoint(String memberPointNo, PointWithdrawRequest request);

}
