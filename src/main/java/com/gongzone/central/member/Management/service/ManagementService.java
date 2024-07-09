package com.gongzone.central.member.Management.service;

import com.gongzone.central.member.Management.domain.MemberPunish;
import com.gongzone.central.member.Management.domain.MemberQuit;
import com.gongzone.central.member.Management.domain.MemberSleep;
import com.gongzone.central.member.domain.Member;
import com.gongzone.central.utils.StatusCode;

import java.util.List;

public interface ManagementService {

    List<Member> getAllMembers();
    List<MemberQuit> getQuitAllMembers();
    List<MemberSleep> getSleepAllMembers();
    List<MemberPunish> getPunishAllMembers();
    void getStatusUpdate(String memberNo, StatusCode statusCode);
    void getPeriodupdate(MemberPunish memberPunish);
    void getPunishInsert(MemberPunish memberPunish);
}
