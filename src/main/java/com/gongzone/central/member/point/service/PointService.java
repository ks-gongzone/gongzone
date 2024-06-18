package com.gongzone.central.member.point.service;

import com.gongzone.central.member.point.domain.PointCharge;
import com.gongzone.central.member.point.domain.PointHistory;
import java.util.List;
import java.util.Map;

public interface PointService {
	Map<String, List<PointHistory>> getAllHistory(String memberNo);

	Map<String, Integer> getCurrentPoint(String memberNo);

	Map<String, String> chargeMemberPoint(String memberNo, PointCharge request);

}
