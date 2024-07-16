package com.gongzone.central.point.service.impl;

import com.gongzone.central.point.domain.PointHistory;
import com.gongzone.central.point.domain.request.PointDTO;
import com.gongzone.central.point.mapper.PointHistoryMapper;
import com.gongzone.central.point.mapper.PointMapper;
import com.gongzone.central.point.service.PointHistoryService;
import com.gongzone.central.utils.MySqlUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointHistoryServiceImpl implements PointHistoryService {

	private final PointMapper pointMapper;
	private final PointHistoryMapper pointHistoryMapper;


	/**
	 * 포인트 충전/인출 시도를 데이터베이스에 기록하고, 해당 기록의 고유 번호를 반환한다.
	 *
	 * @param memberNo 회원 고유번호
	 * @param request  포인트 변동내역
	 * @return 포인트 내역 번호
	 */
	@Override
	public String insert(String memberNo, PointDTO request) {
		String memberPointNo = pointMapper.getMemberPointNo(memberNo);
		calculatePointUpdate(memberPointNo, request);

		String historyPk = MySqlUtil.generatePrimaryKey(pointHistoryMapper.getLastHistoryPk());
		PointHistory pointHistory = PointHistory.builder()
												.pointHistoryNo(historyPk)
												.memberPointNo(memberPointNo)
												.pointHistoryBefore(request.getPointBefore())
												.pointHistoryChange(request.getPointChange())
												.pointHistoryAfter(request.getPointBefore())  // 처음 insert 시 실패를 가정한다. 따라서 before 값 삽입
												.type(request.getChangeType().getCode())
												.build();
		pointHistoryMapper.insertPointHistory(pointHistory);

		return historyPk;
	}

	private void calculatePointUpdate(String memberPointNo, PointDTO request) {
		int current = pointMapper.getCurrentPoint(memberPointNo);
		int after = current + request.getPointChange();

		request.setPointBefore(current);
		request.setPointAfter(after);
	}

	@Override
	public PointHistory get(String pointHistoryNo) {
		return pointHistoryMapper.get(pointHistoryNo);
	}

	@Override
	public List<PointHistory> getMany(String memberNo, int size, int page) {
		String pointNo = pointMapper.getMemberPointNo(memberNo);
		return pointHistoryMapper.getMany(pointNo, size, page - 1);
	}

	@Override
	public void updateSuccess(String historyNo, PointDTO pointCharge) {
		int pointHistoryAfter = pointCharge.getPointAfter();
		pointHistoryMapper.updateHistorySuccess(historyNo, pointHistoryAfter);
	}

}
