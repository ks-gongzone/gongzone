package com.gongzone.central.point.mapper;

import com.gongzone.central.point.domain.Point;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PointMapper {

	void insert(Point point);

	int getCurrentPoint(String memberPointNo);

	Point getPoint(String memberNo);

	void update(@Param("memberPointNo") String memberPointNo,
				@Param("change") int charge);

	String getLastIndex();

	String getMemberPointNo(String memberNo);
	
}
