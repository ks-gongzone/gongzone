package com.gongzone.central.member.point.mapper;

import com.gongzone.central.member.point.domain.Point;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PointMapper {
	Point get(@Param("memberNo") String memberNo);

}
