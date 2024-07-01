package com.gongzone.central.member.socialLogin.kakao.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KakaoRequest {
    private String code;
    private String state;
}
