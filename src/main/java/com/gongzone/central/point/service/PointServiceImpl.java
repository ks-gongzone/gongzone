package com.gongzone.central.point.service;

import com.gongzone.central.point.domain.Point;
import com.gongzone.central.point.domain.PointChange;
import com.gongzone.central.point.domain.PointHistory;
import com.gongzone.central.point.mapper.PointMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
	private final PointTransactionService pointTransactionService;
	private final PointHistoryService pointHistoryService;
	private final PointMapper pointMapper;


	@Override
	public Map<String, List<PointHistory>> getAllHistory(String memberPointNo) {
		List<PointHistory> histories = pointMapper.getAllHistory(memberPointNo);
		Map<String, List<PointHistory>> result = new HashMap<>(
				Map.of("result", histories)
		);

		return result;
	}

	@Override
	public Map<String, Integer> getCurrentPoint(String memberPointNo) {
		Integer point = pointMapper.getCurrentPoint(memberPointNo);
		Map<String, Integer> result = new HashMap<>(
				Map.of("result", point)
		);

		return result;
	}

	@Override
	public Map<String, String> updateMemberPoint(String memberPointNo, PointChange request) throws RuntimeException {
		Map<String, String> response = new HashMap<>();
		PointChange pointChange = pointTransactionService.updateMemberPoint(memberPointNo, request);

		pointHistoryService.insertPointHistory(memberPointNo, pointChange);

		return response;
	}

	// 예시
	@Override
	public Point getPoint(String memberPointNo) {
		return pointMapper.getPointByPointNo(memberPointNo);
	}


}
