package com.gongzone.central.member.login.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginLog {
    private int loginNo;
    private String memberNo;
    private String typeCode;
    private String loginBrowser;
    private Timestamp loginInTime;
    private Date loginOutTime;
    private String statusCode;
    private String userAgent;
}
