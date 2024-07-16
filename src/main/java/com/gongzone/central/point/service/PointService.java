package com.gongzone.central.point.service;

import com.gongzone.central.point.domain.request.PointDTO;

public interface PointService {

	Integer getCurrentPoint(String memberNo);

	void charge(String memberNo, PointDTO request);

	void withdraw(String memberNo, PointDTO request);

	void updatePoint(String memberNo, PointDTO request);

	String getMemberPointNo(String memberNo);

}
