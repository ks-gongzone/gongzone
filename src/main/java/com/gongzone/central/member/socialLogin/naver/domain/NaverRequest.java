package com.gongzone.central.member.socialLogin.naver.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NaverRequest {
    private String code;
    private String state;
}
