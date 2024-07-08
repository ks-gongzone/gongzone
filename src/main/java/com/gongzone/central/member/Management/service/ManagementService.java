package com.gongzone.central.member.Management.service;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.utils.StatusCode;

import java.util.List;

public interface ManagementService {

    List<Member> getAllMembers();
    List<Member> getQuitAllMembers();
    List<Member> getSleepAllMembers();
    List<Member> getPunishAllMembers();
    void getStatusUpdate(String memberNo, StatusCode statusCode);
}
