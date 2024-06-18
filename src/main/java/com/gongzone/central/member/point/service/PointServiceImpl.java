package com.gongzone.central.member.point.service;

import com.gongzone.central.member.point.domain.PointCharge;
import com.gongzone.central.member.point.domain.PointHistory;
import com.gongzone.central.member.point.mapper.PointMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PointServiceImpl implements PointService {
	private final PointMapper pointMapper;

	public PointServiceImpl(PointMapper pointMapper) {
		this.pointMapper = pointMapper;
	}


	@Override
	public Map<String, List<PointHistory>> getAllHistory(String memberNo) {
		List<PointHistory> histories = pointMapper.getAllHistory(memberNo);
		Map<String, List<PointHistory>> result = new HashMap<>(
				Map.of("result", histories)
		);

		return result;
	}

	@Override
	public Map<String, Integer> getCurrentPoint(String memberNo) {
		Integer point = pointMapper.getCurrentPoint(memberNo);
		Map<String, Integer> result = new HashMap<>(
				Map.of("result", point)
		);

		return result;
	}

	@Override
	public Map<String, String> chargeMemberPoint(String memberNo, PointCharge request) {
		String result = null;
		try {
			int amount = request.getAmount();
			pointMapper.chargeMemberPoint(memberNo, amount);
			result = "SUCCESS";
		} catch (Exception ignored) {
			result = "FAILED";
		}
		Map<String, String> response = new HashMap<>(
				Map.of("result", result)
		);
		return response;
	}

}
