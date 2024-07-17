package com.gongzone.central.member.login.service;

import com.gongzone.central.member.login.domain.LoginLog;
import com.gongzone.central.member.login.domain.LoginStatistical;

import java.util.List;

public interface LoginLogService {

    void logLoginAttempt(LoginLog loginLog);

    void logLogout(int loginNo);

    void logLoginFailure(int loginNo);

    LoginLog getLoginNoByMemberNo(String memberNo, String userAgent);

    String getloginBrowserByCode(String userAgent);

    List<LoginStatistical> getLoginStatisticalDate();
}
