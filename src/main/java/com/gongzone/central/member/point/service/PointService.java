package com.gongzone.central.member.point.service;

import com.gongzone.central.member.point.domain.Point;
import java.util.Map;

public interface PointService {
	Map<String, Point> get(String memberNo);

}
