package com.gongzone.central.member.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/*
* socialId 아이디
* provider 소셜 타입
* name 이름
* email 이메일
* accessToken 액세스 토큰
* refreshToken 리프레시 토큰
* accessTokenExpiry 액세스 토큰 만료 시간
* refreshTokenExpiry 리프레시 토큰 만료 시간
* */
@Data
@Builder
public class SocialMember {
    private String socialId;
    private String provider;
    private String name;
    private String email;
    private String accessToken;
    private String refreshToken;
    private Date accessTokenExpiry;
    private Date refreshTokenExpiry;
}
