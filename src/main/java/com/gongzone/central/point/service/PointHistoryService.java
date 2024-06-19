package com.gongzone.central.point.service;

import com.gongzone.central.point.domain.PointChange;
import com.gongzone.central.point.domain.PointHistory;
import com.gongzone.central.point.mapper.PointMapper;
import com.gongzone.central.utils.MySqlUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointHistoryService {
	private final PointMapper pointMapper;


	public String insertPointHistory(String memberPointNo, PointChange pointChange) {
		String last = pointMapper.getLastHistoryPk();
		PointHistory pointHistory = PointHistory.builder()
												.pointHistoryNo(MySqlUtil.generatePrimaryKey(last))
												.memberPointNo(memberPointNo)
												.pointHistoryBefore(pointChange.getPointBefore())
												.pointHistoryChange(pointChange.getPointChange())
												.pointHistoryAfter(pointChange.getPointAfter())
												.type(pointChange.getChangeType())
												.status(pointChange.getChangeStatus())
												.build();

		String result;
		try {
			pointMapper.insertPointHistory(pointHistory);
			result = "SUCCESS";
		} catch (Exception ignored) {
			result = "FAILED";
		}
		return result;
	}

}
