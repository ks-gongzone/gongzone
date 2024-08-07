package com.gongzone.central.member.report.service;

import com.gongzone.central.member.report.domain.ReportMember;
import com.gongzone.central.member.report.domain.ReportStatistical;
import com.gongzone.central.utils.StatusCode;

import java.util.List;

public interface ReportService {

    List<ReportMember> getAllReportMember();

    void getReportStatusUpdate(int memberReportNo, StatusCode statusCode);

    void getStatusUpdate(String memberNo, StatusCode statusCode);

    void getReportMemberInsert(ReportMember reportMember);

    List<ReportStatistical> getReportStatisticalDate();
}
