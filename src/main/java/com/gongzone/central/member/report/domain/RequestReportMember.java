package com.gongzone.central.member.report.domain;

import lombok.Data;

@Data
public class RequestReportMember {
    private int memberReportNo;
    private String memberNo;
    private String statusCode;
}
