package com.gongzone.central.point.mapper;

import com.gongzone.central.point.domain.PointHistory;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PointHistoryMapper {

	void insert(PointHistory pointHistory);

	PointHistory get(String pointHistoryNo);

	List<PointHistory> getMany(@Param("memberPointNo") String memberPointNo,
							   @Param("size") int size,
							   @Param("page") int page);

	void updateHistorySuccess(@Param("historyNo") String historyNo,
							  @Param("pointHistoryAfter") int pointHistoryAfter);

	String getLastIndex();

}
