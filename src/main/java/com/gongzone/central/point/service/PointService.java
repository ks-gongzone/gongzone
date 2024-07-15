package com.gongzone.central.point.service;

import com.gongzone.central.point.domain.request.PointRequest;

public interface PointService {

	Integer getCurrentPoint(String memberNo);

	void charge(String memberNo, PointRequest request);

	void withdraw(String memberNo, PointRequest request);

	void update(String memberNo, PointRequest request);

	String getPointNo(String memberNo);

}
