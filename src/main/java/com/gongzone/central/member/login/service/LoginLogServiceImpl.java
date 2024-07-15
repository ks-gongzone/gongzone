package com.gongzone.central.member.login.service;

import com.gongzone.central.member.login.domain.LoginLog;
import com.gongzone.central.member.login.mapper.LoginMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginLogServiceImpl implements LoginLogService{

    private final LoginMapper loginMapper;

    @Override
    public void logLoginAttempt(LoginLog loginLog) {
        loginMapper.loginLogInsert(loginLog);
    }

    @Override
    public void logLogout(int loginNo) {
        System.out.println("loginNo : " + loginNo);
        loginMapper.logoutLogUpdate(loginNo);
    }

    @Override
    public void logLoginFailure(int loginNo) {
        System.out.println("loginNo : " + loginNo);
        loginMapper.loginFalseLogUpdate(loginNo);
    }

    @Override
    public int getLoginNoByMemberNo(String memberNo) {
        return loginMapper.loginNoBymemberNo(memberNo);
    }

    @Override
    public String getloginBrowserByCode(String userAgent) {
        if (userAgent.contains("Whale")) {
            return "Whale";
        } else if (userAgent.contains("Chrome") && !userAgent.contains("Edg")) {
            return "Chrome";
        } else if (userAgent.contains("Edg")) {
            return "Edge";
        } else if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            return "Internet Explorer";
        } else {
            return "Unknown";
        }
    }
}
