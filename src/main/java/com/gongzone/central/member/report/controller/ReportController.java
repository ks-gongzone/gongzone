package com.gongzone.central.member.report.controller;


import com.gongzone.central.member.Management.domain.MemberPunish;
import com.gongzone.central.member.Management.service.ManagementService;
import com.gongzone.central.member.report.domain.ReportMember;
import com.gongzone.central.member.report.domain.ReportStatistical;
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
    private final ManagementService managementService;

    @GetMapping("/ReportMember/listReportAll")
    public List<ReportMember> listReportALl() {
        List<ReportMember> reportMember = reportService.getAllReportMember();
        return reportMember;
    }

    @PostMapping("/ReportstatusUpdate/{memberNo}")
    public String statusUpdate(@PathVariable String memberNo, @RequestBody RequestReportMember requestReportMember) {
        StatusCode statusCode = StatusCode.fromCode(requestReportMember.getStatusCode());
        reportService.getReportStatusUpdate(requestReportMember.getMemberReportNo(), statusCode);
        return statusCode.toString();
    }

    @PostMapping("/ReportMember/punish/{memberNo}")
    public ResponseEntity<Boolean> punishInsert(@PathVariable String memberNo, @RequestBody ReportMember reportMember) {
        MemberPunish memberPunish = MemberPunish.builder()
                .memberNo(memberNo)
                .memberAdminNo(reportMember.getMemberAdminNo())
                .punishType(reportMember.getTypeCode())
                .punishReason(reportMember.getReasonDetail())
                .punishPeriod(reportMember.getPeriod())
                .punishStatus(reportMember.getStatusCode())
                .memberStatus(reportMember.getMemberStatusCode())
                .build();

        try {
            managementService.getPunishInsert(memberPunish);

            StatusCode memberStatusCode = StatusCode.fromCode(reportMember.getMemberStatusCode());
            reportService.getStatusUpdate(memberNo, memberStatusCode);

            StatusCode reportStatusCode = StatusCode.fromCode(reportMember.getStatusCode());
            reportService.getReportStatusUpdate(reportMember.getMemberReportNo(), reportStatusCode);

            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @PostMapping("/ReportMember/report")
    public ResponseEntity<Boolean> reportMemberInsert(@RequestBody ReportMember reportMember) {
        try {
            reportService.getReportMemberInsert(reportMember);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @PostMapping("/admin/statistical/report")
    public List<ReportStatistical> adminReportStatisticalDate() {
        List<ReportStatistical> reportStatisticals = reportService.getReportStatisticalDate();
        return reportStatisticals;
    }
}
