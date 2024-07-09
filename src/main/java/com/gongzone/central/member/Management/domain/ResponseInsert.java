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
    private String punishReason;
    private String punishPeriod;
    private String statusCode;
    private String memberStatusCode;
}
