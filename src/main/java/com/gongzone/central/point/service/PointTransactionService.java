package com.gongzone.central.point.service;

import com.gongzone.central.point.domain.PointChangeRequest;
import com.gongzone.central.point.mapper.PointMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointTransactionService {
	private final PointMapper pointMapper;


	/**
	 * 회원의 기존 포인트를 이용해 변동량, 변동 전/후를 계산한다.
	 *
	 * @param memberPointNo      회원 포인트 번호
	 * @param pointChangeRequest 포인트 변동량
	 */
	public void calculatePointUpdate(String memberPointNo, PointChangeRequest pointChangeRequest) {
		int current = pointMapper.getCurrentPoint(memberPointNo);
		int after = current + pointChangeRequest.getPointChange();

		pointChangeRequest.setPointBefore(current);
		pointChangeRequest.setPointAfter(after);
	}

	/**
	 * 미리 계산된 포인트 변동 객체를 이용해 실제로 포인트를 변화시킨다.
	 *
	 * @param memberPointNo
	 * @param pointChangeRequest
	 */
	public void updatePoint(String memberPointNo, PointChangeRequest pointChangeRequest) {
		int change = pointChangeRequest.getPointChange();
		System.out.println("change = " + change);
		pointMapper.updatePoint(memberPointNo, change);
	}

}
