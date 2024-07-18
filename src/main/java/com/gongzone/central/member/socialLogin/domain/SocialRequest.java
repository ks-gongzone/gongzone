package com.gongzone.central.member.socialLogin.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocialRequest {
    private String code;
    private String state;
    private String userAgent;
}
