package com.gongzone.central.point.mapper;

import com.gongzone.central.point.domain.PointHistory;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PointHistoryMapper {

	String getLastHistoryPk();

	List<PointHistory> getHistories(@Param("memberPointNo") String memberPointNo,
									@Param("size") int size,
									@Param("page") int page);

	PointHistory getHistory(@Param("memberPointNo") String memberPointNo,
							@Param("pointHistoryNo") String pointHistoryNo);

	void insertPointHistory(PointHistory pointHistory);

	void updateHistorySuccess(@Param("historyNo") String historyNo,
							  @Param("pointHistoryAfter") int pointHistoryAfter);

}
