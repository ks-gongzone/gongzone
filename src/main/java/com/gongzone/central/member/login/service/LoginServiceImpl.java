package com.gongzone.central.member.login.service;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.login.domain.LoginResponse;
import com.gongzone.central.member.login.domain.LoginRequest;
import com.gongzone.central.member.login.mapper.LoginMapper;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private final LoginMapper loginMapper;

    public LoginServiceImpl(LoginMapper loginMapper) {
        this.loginMapper = loginMapper;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Member member = loginMapper.getMemberFromId(loginRequest.getLoginId()); //여기가 문제다
        if (member != null && member.getMemberPw().equals(loginRequest.getLoginPw())) {
            return new LoginResponse(member.getMemberId());
        } else {
            throw new IllegalArgumentException("없는 계정");
        }
    }

    /*@Override
    public boolean login(LoginRequest login) {
        LoginRequest loginRequest = loginMapper.getMemberFromId(login.getLoginId());
        if (loginRequest != null && loginRequest.getLoginPw().equals(login.getLoginPw())) {
            return true;
        } else {
            return false;
        }
    }*/
}
