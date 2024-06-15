package com.gongzone.central.member.point.service;

import com.gongzone.central.member.point.domain.Point;
import com.gongzone.central.member.point.mapper.PointMapper;
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
	public Point get(String memberNo) {
		return pointMapper.get(memberNo);
	}

}
