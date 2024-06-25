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
// 다른건
// 임시는 순서 아이디 소셜타입 이순서가 본인 마음대로 해도 상과없는데
// 실제 db에존재하는 dto들은 순서가 똑같아야해요 왜그러냐
//mapper 레파지토리 mysql에 mybatis방식으로 해서 mapper를쓰기 때문에