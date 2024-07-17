package com.gongzone.central.member.report.service;

import com.gongzone.central.member.report.domain.ReportMember;
import com.gongzone.central.member.report.domain.ReportStatistical;
import com.gongzone.central.member.report.mapper.ReportMapper;
import com.gongzone.central.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportMapper reportMapper;

    @Override
    public List<ReportMember> getAllReportMember() {
        return reportMapper.findReportAll();
    }

    // 제재하는경우 인서트문 필요
    @Override
    public void getReportStatusUpdate(int memberReportNo, StatusCode statusCode) {
        if (statusCode == StatusCode.S010601 || statusCode == StatusCode.S010602 || statusCode == StatusCode.S010603) {
            reportMapper.updateReportStatus(memberReportNo, statusCode);
        }
    }

    @Override
    public void getStatusUpdate(String memberNo, StatusCode statusCode) {
        if (statusCode == StatusCode.S010101 || statusCode == StatusCode.S010102 || statusCode == StatusCode.S010103 || statusCode == StatusCode.S010104) {
            reportMapper.updateStatus(memberNo, statusCode);
        }
    }

    @Override
    public void getReportMemberInsert(ReportMember reportMember) {
        reportMapper.insertReport(reportMember);
    }

    @Override
    public List<ReportStatistical> getReportStatisticalDate() {
        return reportMapper.reportInTimeDay();
    }
}
