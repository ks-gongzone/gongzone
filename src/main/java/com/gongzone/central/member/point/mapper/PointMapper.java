package com.gongzone.central.member.point.mapper;

import com.gongzone.central.member.point.domain.PointHistory;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PointMapper {
	List<PointHistory> getAllHistory(String memberNo);

	Integer getCurrentPoint(String memberNo);

}
