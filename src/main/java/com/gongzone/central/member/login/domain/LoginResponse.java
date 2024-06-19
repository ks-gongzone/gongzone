package com.gongzone.central.member.login.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResponse {
    private String grantType;
    private String accessToken;
    private long tokenExpiresIn;
    private String errorMessage;

    public LoginResponse(String grantType, String accessToken, long tokenExpiresIn, String errorMessage) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.tokenExpiresIn = tokenExpiresIn;
        this.errorMessage = errorMessage;
    }

    public LoginResponse(String errorMessage) {
        this(null, null, 0, errorMessage);
    }
}