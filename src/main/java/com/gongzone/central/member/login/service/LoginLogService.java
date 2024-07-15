package com.gongzone.central.member.login.service;

import com.gongzone.central.member.login.domain.LoginLog;

public interface LoginLogService {

    void logLoginAttempt(LoginLog loginLog);
    void logLogout(int loginNo);
    void logLoginFailure(int loginNo);
    int getLoginNoByMemberNo(String memberNo);

    String getloginBrowserByCode(String userAgent);
}
