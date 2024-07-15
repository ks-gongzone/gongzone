package com.gongzone.central.member.login.mapper;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.login.domain.LoginLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {
    Member getMemberById(String loginId);
    Member getMemberByEmail(String email);
    Member getMemberByNo(String memberNo);
    //String getPointNoByMemberNo(String memberNo);

    void loginLogInsert(LoginLog loginLog);
    int logoutLogUpdate(int loginNo);
    int loginFalseLogUpdate(int loginNo);
    LoginLog loginNoBymemberNo(String memberNo, String userAgent);
}
