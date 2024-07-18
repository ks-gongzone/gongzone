package com.gongzone.central.point.service.impl;


import com.gongzone.central.point.domain.Point;
import com.gongzone.central.point.domain.request.PointDTO;
import com.gongzone.central.point.mapper.PointMapper;
import com.gongzone.central.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

	private final PointMapper pointMapper;


	@Override
	public Integer getCurrentPoint(String memberNo) {
		String memberPointNo = getMemberPointNo(memberNo);
		return pointMapper.getCurrentPoint(memberPointNo);
	}

	@Override
	public Point getPoint(String memberNo) {
		String memberPointNo = getMemberPointNo(memberNo);
		return pointMapper.getPoint(memberPointNo);
	}

	@Override
	public String getMemberPointNo(String memberNo) {
		return pointMapper.getMemberPointNo(memberNo);
	}

	@Override
	public void updatePoint(String memberNo, PointDTO request) {
		String memberPointNo = getMemberPointNo(memberNo);
		int change = request.getPointChange();

		pointMapper.update(memberPointNo, change);
	}

}
