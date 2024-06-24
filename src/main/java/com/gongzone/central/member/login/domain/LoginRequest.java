package com.gongzone.central.member.login.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String loginId;   // 아이디
    private String loginPw;     // 비번
}
