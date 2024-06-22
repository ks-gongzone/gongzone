package com.gongzone.central.member.login.mapper;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.login.domain.LoginRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LoginMapper {
    Member getMemberFromId(String loginId);

    //String getPointNoByMemberNo(String memberNo);

}
