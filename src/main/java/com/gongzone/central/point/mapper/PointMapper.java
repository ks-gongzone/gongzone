package com.gongzone.central.point.mapper;

import com.gongzone.central.point.domain.Point;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PointMapper {

	Integer getCurrentPoint(String memberPointNo);

	void updatePoint(@Param("memberPointNo") String memberPointNo,
					 @Param("change") int change);

	Point getPointNoByMemberNo(String memberNo);

	String getPointNo(String memberNo);

	void insertPoint(Point point);

	String getLastMemberPointNo();

}
