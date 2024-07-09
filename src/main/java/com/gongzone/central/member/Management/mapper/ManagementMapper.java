package com.gongzone.central.member.Management.mapper;

import com.gongzone.central.member.Management.domain.MemberPunish;
import com.gongzone.central.member.Management.domain.MemberQuit;
import com.gongzone.central.member.Management.domain.MemberSleep;
import com.gongzone.central.member.domain.Member;
import com.gongzone.central.utils.StatusCode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ManagementMapper {

    List<Member> findAll();
    List<MemberQuit> findQuitList();
    List<MemberSleep> findSleepList();
    List<MemberPunish> findPunishList();
    void updateStatus(String memberNo, StatusCode statusCode);
    void updatePunish(MemberPunish memberPunish);
    void insertPunish(MemberPunish memberPunish);
}
