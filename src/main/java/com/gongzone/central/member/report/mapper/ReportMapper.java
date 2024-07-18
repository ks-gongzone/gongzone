package com.gongzone.central.member.report.mapper;

import com.gongzone.central.member.report.domain.ReportMember;
import com.gongzone.central.member.report.domain.ReportStatistical;
import com.gongzone.central.utils.StatusCode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReportMapper {
    List<ReportMember> findReportAll();

    void updateReportStatus(int memberReportNo, StatusCode statusCode);

    void updateStatus(String memberNo, StatusCode statusCode);

    void insertReport(ReportMember reportMember);

    List<ReportStatistical> reportInTimeDay();
}
