package com.gongzone.central.point.mapper;

import com.gongzone.central.point.domain.Point;
import com.gongzone.central.point.domain.PointHistory;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PointMapper {
	List<PointHistory> getAllHistory(String memberPointNo);

	Integer getCurrentPoint(String memberPointNo);

	void updateMemberPoint(@Param("memberPointNo") String memberPointNo,
						   @Param("change") int change);

	void insertPointHistory(PointHistory pointHistory);

	String getLastHistoryPk();

	Point getPointNoByMemberNo(String memberNo);

	Point getPointByPointNo(String memberPointNo); //예시

}
