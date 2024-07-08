package com.gongzone.central.member.Management.service;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.utils.StatusCode;

import java.util.List;

public interface ManagementService {

    List<Member> getAllMembers();
    //Member getMemberByNo(String memberNo);
    Member getMemberByStatus(String memberNo);
    void getStatusUpdate(String memberNo, StatusCode statusCode);
}
