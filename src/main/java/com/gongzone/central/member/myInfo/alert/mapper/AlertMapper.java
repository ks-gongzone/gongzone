package com.gongzone.central.member.myInfo.alert.mapper;

import com.gongzone.central.member.myInfo.alert.domain.MyAlert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AlertMapper {
    MyAlert findAlertByMemberNo(@Param("memberNo") String memberNo);
    void updateAlertSettings(MyAlert myAlert);
}
