package com.gongzone.central.member.login.domain;

import lombok.Data;

@Data
public class RefreshTokenResponse {
    private String newAccessToken;
    private long expiresIn;
    private String refreshToken;

    public RefreshTokenResponse(String newAccessToken, long expiresIn, String refreshToken) {
        this.newAccessToken = newAccessToken;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
    }
}
