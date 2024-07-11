package com.gongzone.central.member.report.domain;

import lombok.Data;

import java.util.Date;

@Data
public class ReportMember {
    private int memberReportNo;
    private String memberNo;
    private String memberTargetNo;
    private String typeCode;
    private String memberReportReason;
    private Date memberReportDate;  //신고일시
    private String statusCode;
    private String statusReason;

    private String memberAdminNo;
    private String reasonDetail;
    private String period;
    private String memberStatusCode;
}
