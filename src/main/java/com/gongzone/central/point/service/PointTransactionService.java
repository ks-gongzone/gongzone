package com.gongzone.central.point.service;

import com.gongzone.central.point.domain.request.PointRequest;
import com.gongzone.central.point.mapper.PointMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointTransactionService {
	private final PointMapper pointMapper;

	
	/**
	 * 포인트 변동 객체를 이용해 실제로 포인트를 변화시킨다.
	 *
	 * @param memberPointNo
	 * @param request
	 */
	public void updatePoint(String memberPointNo, PointRequest request) {
		int change = request.getPointChange();
		pointMapper.updatePoint(memberPointNo, change);
	}

}
