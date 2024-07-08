package com.gongzone.central.member.Management.mapper;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.utils.StatusCode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ManagementMapper {

    List<Member> findAll();
    List<Member> findQuitList();
    List<Member> findSleepList();
    List<Member> findPunishList();
    void updateStatus(String memberNo, StatusCode statusCode);
}
