package com.gongzone.central.member.socialLogin.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class SocialMember {
    private String socialId;            // 아이디
    private String provider;            // 소셜 타입
    private String name;                // 이름
    private String email;               // 이메일
    private String accessToken;         // 액세스 토큰
    private String refreshToken;        // 리프레시 토큰
    private Date accessTokenExpiry;     // 액세스 토큰 만료 시간
    private Date refreshTokenExpiry;    // 리프레시 토큰 만료 시간
}