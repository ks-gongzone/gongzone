package com.gongzone.central.member.login.service;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.login.domain.LoginRequest;


public interface LoginService {
    //Member login(LoginRequest loginRequest);

    Member login(String loginId, String loginPw);
}
