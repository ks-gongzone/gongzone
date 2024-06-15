package com.gongzone.central.member.point.service;

import com.gongzone.central.member.point.domain.Point;
import com.gongzone.central.member.point.mapper.PointMapper;
import java.util.HashMap;
import java.util.Map;
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
	public Map<String, Point> get(String memberNo) {
		Point point = pointMapper.get(memberNo);
		Map<String, Point> result = new HashMap<>(
				Map.of("result", point)
		);

		return result;
	}

}
