package com.gongzone.central.point.service;

import com.gongzone.central.point.domain.PointChangeRequest;
import com.gongzone.central.point.domain.PointHistory;
import com.gongzone.central.point.mapper.PointMapper;
import com.gongzone.central.utils.MySqlUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointHistoryService {
	private final PointMapper pointMapper;


	/**
	 * 회원의 포인트 충전 시도를 데이터베이스에 기록하고, 해당 내역 번호를 반환한다.
	 *
	 * @param memberPointNo      회원 포인트 번호
	 * @param pointChangeRequest 포인트 변동 내역
	 * @return 포인트 내역번호
	 */
	public String insertPointHistory(String memberPointNo, PointChangeRequest pointChangeRequest) {
		String last = pointMapper.getLastHistoryPk();
		PointHistory pointHistory = PointHistory.builder()
												.pointHistoryNo(MySqlUtil.generatePrimaryKey(last))
												.memberPointNo(memberPointNo)
												.pointHistoryBefore(pointChangeRequest.getPointBefore())
												.pointHistoryChange(pointChangeRequest.getPointChange())
												.pointHistoryAfter(pointChangeRequest.getPointBefore())  // 처음 insert 시 실패를 가정한다. 따라서 before 값 삽입
												.type(pointChangeRequest.getChangeType())
												.build();
		pointMapper.insertPointHistory(pointHistory);

		return pointHistory.getPointHistoryNo();
	}

	/**
	 * 포인트 충전 상태를 성공으로 변경한다.
	 *
	 * @param historyNo   포인트내역번호
	 * @param pointCharge 포인트 변동 객체
	 */
	public void updateHistorySuccess(String historyNo, PointChangeRequest pointCharge) {
		int pointHistoryAfter = pointCharge.getPointAfter();
		pointMapper.updateHistorySuccess(historyNo, pointHistoryAfter);
	}

}
