package com.gongzone.central.member.report.service;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.report.domain.ReportMember;
import com.gongzone.central.utils.StatusCode;

import java.util.List;

public interface ReportService {

    List<ReportMember> getAllReportMember();
    void getReportStatusUpdate(int memberReportNo, StatusCode statusCode);
}
