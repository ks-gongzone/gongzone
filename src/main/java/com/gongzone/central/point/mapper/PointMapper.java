package com.gongzone.central.point.mapper;

import com.gongzone.central.point.domain.Point;
import com.gongzone.central.point.domain.PointHistory;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PointMapper {
	List<PointHistory> getHistories(@Param("memberPointNo") String memberPointNo,
									@Param("size") int size,
									@Param("page") int page);

	PointHistory getHistory(@Param("memberPointNo") String memberPointNo,
							@Param("pointHistoryNo") String pointHistoryNo);

	Integer getCurrentPoint(String memberPointNo);

	void updatePoint(@Param("memberPointNo") String memberPointNo,
					 @Param("change") int change);

	void insertPointHistory(PointHistory pointHistory);

	void updateHistorySuccess(@Param("historyNo") String historyNo,
							  @Param("pointHistoryAfter") int pointHistoryAfter);

	String getLastHistoryPk();

	Point getPointNoByMemberNo(String memberNo);

	void insertPoint(Point point);

	String getLastMemberPointNo();

}
