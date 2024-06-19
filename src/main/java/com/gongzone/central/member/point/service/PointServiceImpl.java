package com.gongzone.central.member.point.service;

import static com.gongzone.central.utils.StatusCode.STATUS_POINT_HISTORY_FAILED;
import static com.gongzone.central.utils.StatusCode.STATUS_POINT_HISTORY_SUCCESS;
import static com.gongzone.central.utils.TypeCode.TYPE_POINT_INCREASE_CHARGE;

import com.gongzone.central.member.point.domain.PointCharge;
import com.gongzone.central.member.point.domain.PointHistory;
import com.gongzone.central.member.point.mapper.PointMapper;
import com.gongzone.central.utils.MySqlUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
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
	public Map<String, String> chargeMemberPoint(String memberPointNo, PointCharge request) {
		PointHistory pointHistory = new PointHistory();
		pointHistory.setMemberPointNo(memberPointNo);
		int current = pointMapper.getCurrentPoint(memberPointNo);
		pointHistory.setPointHistoryBefore(String.valueOf(current));
		pointHistory.setPointHistoryChange(String.valueOf(request.getAmount()));
		pointHistory.setPointHistoryAfter(String.valueOf(current + request.getAmount()));
		pointHistory.setTypeCode(TYPE_POINT_INCREASE_CHARGE.getCode());

		String status = null;
		try {
			int amount = request.getAmount();
			pointMapper.chargeMemberPoint(memberPointNo, amount);
			status = "SUCCESS";
			pointHistory.setStatusCode(STATUS_POINT_HISTORY_SUCCESS.getCode());
		} catch (Exception ignored) {
			status = "FAILED";
			pointHistory.setStatusCode(STATUS_POINT_HISTORY_FAILED.getCode());
		}
		insertPointHistory(pointHistory);
		Map<String, String> response = new HashMap<>(
				Map.of("result", status)
		);

		return response;
	}

	@Override
	public void insertPointHistory(PointHistory pointHistory) {
		String last = pointMapper.getLastHistoryPk();
		String pointHistoryNo = MySqlUtil.generatePrimaryKey(last);
		pointHistory.setPointHistoryNo(pointHistoryNo);

		pointMapper.insertPointHistory(pointHistory);
	}

}
