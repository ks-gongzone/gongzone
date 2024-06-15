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
        Member member = loginMapper.getMemberFromId(loginRequest.getLoginId());
        if (member != null) {
            if(member.getMemberPw().equals(loginRequest.getLoginPw())) {
                return new LoginResponse("로그인 성공" + member.getMemberId());
            } else {
                throw new IllegalArgumentException("비밀번호 다름");
            }
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
