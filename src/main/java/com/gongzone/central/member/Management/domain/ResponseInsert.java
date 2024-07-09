package com.gongzone.central.member.Management.domain;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class ResponseInsert {
    private String memberNo;
    private String memberAdminNo;
    private String typeCode;
    private String reasonDetail;
    private String period;
    private String statusCode;
    private String memberStatusCode;
}
