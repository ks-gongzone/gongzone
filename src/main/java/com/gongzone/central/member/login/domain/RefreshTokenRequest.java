package com.gongzone.central.member.login.domain;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;
}