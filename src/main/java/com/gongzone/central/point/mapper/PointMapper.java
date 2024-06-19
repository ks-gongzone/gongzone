package com.gongzone.central.point.mapper;

import com.gongzone.central.point.domain.PointHistory;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PointMapper {
	List<PointHistory> getAllHistory(String memberPointNo);

	Integer getCurrentPoint(String memberPointNo);

	void chargeMemberPoint(@Param("memberPointNo") String memberPointNo,
						   @Param("amount") int amount);

	void withdrawMemberPoint(@Param("memberPointNo") String memberPointNo,
							 @Param("amount") int amount);

	void insertPointHistory(PointHistory pointHistory);

	String getLastHistoryPk();

}
