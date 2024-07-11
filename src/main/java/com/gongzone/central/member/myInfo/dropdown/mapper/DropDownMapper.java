package com.gongzone.central.member.myInfo.dropdown.mapper;

import com.gongzone.central.member.myInfo.dropdown.domain.DropDownInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DropDownMapper {
    DropDownInfo findByData(@Param("memberNo") String memberNo, @Param("pointNo") String pointNo);
}
