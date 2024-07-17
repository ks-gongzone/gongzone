package com.gongzone.central.member.login.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginStatistical {
    private String loginInTimeDateFormat;
    private int total;
    private int direct;
    private int social;
}
