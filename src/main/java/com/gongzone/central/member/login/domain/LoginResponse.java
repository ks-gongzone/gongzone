package com.gongzone.central.member.login.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String loginToken;
    private String message;


    // JWT 토큰만 반환하는 경우를 위한 생성자
    public LoginResponse(String loginToken) {
        this.loginToken = loginToken;
    }
}
