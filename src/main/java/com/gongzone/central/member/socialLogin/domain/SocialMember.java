package com.gongzone.central.member.socialLogin.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class SocialMember {
    private String provider;            // 소셜 타입
    private String name;                // 이름
    private String email;               // 이메일
    private String phoneNumber;         // 전화번호
    private String gender;              // 성별
    private String tokenUri;            // 토큰 uri
    private String socialAccessToken;   // 소셜 액세스 토큰
    private String socialRefreshToken;  // 소셜 리프레시 토큰
    private Date accessTokenExpiry;     // 액세스 토큰 만료 시간
    private Date refreshTokenExpiry;    // 리프레시 토큰 만료 시간
    private String accessToken;
    private String memberNo;
    private String pointNo;
    private String refreshToken;
    private String userAgent;
    private long tokenExpiresIn;
}