package com.gongzone.central.point.service;

import com.gongzone.central.point.domain.request.PointDTO;

public interface PointService {

	Integer getCurrentPoint(String memberNo);

	void updatePoint(String memberNo, PointDTO request);

	String getMemberPointNo(String memberNo);

}
