package com.gongzone.central.point.mapper;

import com.gongzone.central.point.domain.Point;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PointMapper {

	void insert(Point point);

	String getLastIndex();

	Point getPoint(String memberNo);

	int getCurrentPoint(String memberPointNo);

	String getMemberPointNo(String memberNo);

	void update(@Param("memberPointNo") String memberPointNo,
				@Param("change") int charge);

}
