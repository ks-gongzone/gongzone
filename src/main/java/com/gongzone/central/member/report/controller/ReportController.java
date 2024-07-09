package com.gongzone.central.member.report.controller;


import com.gongzone.central.member.Management.domain.ManagementRequest;
import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.report.domain.ReportMember;
import com.gongzone.central.member.report.domain.RequestReportMember;
import com.gongzone.central.member.report.service.ReportService;
import com.gongzone.central.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/ReportMember/listReportAll")
    public List<ReportMember> listReportALl() {
        System.out.println("1111111111111111111111111");
        List<ReportMember> reportMember = reportService.getAllReportMember();
        System.out.println("reportMember : " + reportMember.size());
        return reportMember;
    }

    @PostMapping("/ReportstatusUpdate/{memberNo}")
    public String statusUpdate(@PathVariable String memberNo, @RequestBody RequestReportMember requestReportMember) {
        System.out.println("11111111111111111111111111");
        StatusCode statusCode = StatusCode.fromCode(requestReportMember.getStatusCode());
        System.out.println("statusCode : " + statusCode);
        System.out.println("reportMember.getMemberReportNo : " + requestReportMember.getMemberReportNo());
        reportService.getReportStatusUpdate(requestReportMember.getMemberReportNo(), statusCode);
        System.out.println("statusCode : " + statusCode);
        System.out.println("reportMember : " + requestReportMember.toString());
        return statusCode.toString();
    }
}
