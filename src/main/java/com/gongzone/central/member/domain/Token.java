package com.gongzone.central.member.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Token {
    private int tokenNo;
    private String memberNo;
    private String tokenType;
    private String tokenValueAcc;
    private String tokenValueRef;
    private Date tokenExpiresAcc;
    private Date tokenExpiresRef;
    private Date tokenLastUpdate;
}
