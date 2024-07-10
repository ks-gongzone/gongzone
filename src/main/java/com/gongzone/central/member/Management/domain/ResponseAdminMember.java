package com.gongzone.central.member.Management.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ResponseAdminMember {
    private String memberNo;
    private String typeCode;
    private String reasonDetail;
    private String period;
}
