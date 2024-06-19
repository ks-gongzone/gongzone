package com.gongzone.central.point.service;

import static com.gongzone.central.utils.StatusCode.STATUS_POINT_HISTORY_FAILED;
import static com.gongzone.central.utils.StatusCode.STATUS_POINT_HISTORY_SUCCESS;

import com.gongzone.central.point.domain.PointChange;
import com.gongzone.central.point.mapper.PointMapper;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointTransactionService {
	private final PointMapper pointMapper;

	public PointChange updateMemberPoint(String memberPointNo, PointChange pointChange) throws SQLException {
		int current = pointMapper.getCurrentPoint(memberPointNo);
		int change = pointChange.getPointChange();
		int after = current + change;
		String type = pointChange.getChangeType();
		String status;
		try {
			pointMapper.updateMemberPoint(memberPointNo, change);
			status = STATUS_POINT_HISTORY_SUCCESS.getCode();
		} catch (Exception ignored) {
			status = STATUS_POINT_HISTORY_FAILED.getCode();
		}
		
		pointChange.setPointBefore(current);
		pointChange.setPointChange(change);
		pointChange.setPointAfter(after);
		pointChange.setChangeType(type);
		pointChange.setChangeStatus(status);

		return pointChange;
	}

}
