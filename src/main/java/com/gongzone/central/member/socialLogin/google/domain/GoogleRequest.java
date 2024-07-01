package com.gongzone.central.member.socialLogin.google.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleRequest {
    private String code;
    private String state;
}
