package com.gongzone.central.member.point.mapper;

import com.gongzone.central.member.point.domain.PointHistory;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PointMapper {
    List<PointHistory> getAllHistory(String memberPointNo);

    Integer getCurrentPoint(String memberPointNo);

    void chargeMemberPoint(@Param("memberPointNo") String memberPointNo,
                           @Param("amount") int amount);

    void insertPointHistory(PointHistory pointHistory);

    String getLastHistoryPk();

}
