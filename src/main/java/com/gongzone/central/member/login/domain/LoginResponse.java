package com.gongzone.central.member.login.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResponse {
    private String grantType;
    private String accessToken;
    private long tokenExpiresIn;
    private String refreshToken;
    private String memberNo;
    private String pointNo;
    private String errorMessage;

    public LoginResponse(String grantType, String accessToken, long tokenExpiresIn, String refreshToken, String memberNo, String pointNo, String errorMessage) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.tokenExpiresIn = tokenExpiresIn;
        this.refreshToken = refreshToken;
        this.memberNo = memberNo;
        this.pointNo = pointNo;
        this.errorMessage = errorMessage;
    }

    public LoginResponse(String errorMessage) {
        this(null, null, 0, null, null, null, errorMessage);
    }
}