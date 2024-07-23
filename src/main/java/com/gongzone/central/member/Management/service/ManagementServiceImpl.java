package com.gongzone.central.member.Management.service;

import com.gongzone.central.member.Management.domain.MemberPunish;
import com.gongzone.central.member.Management.domain.MemberQuit;
import com.gongzone.central.member.Management.domain.MemberSleep;
import com.gongzone.central.member.Management.mapper.ManagementMapper;
import com.gongzone.central.member.domain.Member;
import com.gongzone.central.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagementServiceImpl implements ManagementService {

    private final ManagementMapper managementMapper;

    @Override
    public List<Member> getAllMembers() {
        List<Member> lists = managementMapper.findAll();
        List<Member> converList = new ArrayList<Member>();
        lists.forEach(member -> {
            converList.add(
                    Member.builder()
                            .memberNo(member.getMemberNo())
                            .memberLevel(member.getMemberLevel())
                            .memberEmail(member.getMemberEmail())
                            .memberStatus(StatusCode.getDescriptionByCode(member.getMemberStatus()))
                            .memberId(member.getMemberId())
                            .memberPhone(member.getMemberPhone())
                            .memberGender(member.getMemberGender())
                            .memberAddress(member.getMemberAddress())
                            .memberBirthday(member.getMemberBirthday())
                            .memberNick(member.getMemberNick())
                            .build()
            );
        });
        return converList;
    }

    @Override
    public List<MemberQuit> getQuitAllMembers() {
        return managementMapper.findQuitList();
    }

    @Override
    public List<MemberSleep> getSleepAllMembers() {
        return managementMapper.findSleepList();
    }

    @Override
    public List<MemberPunish> getPunishAllMembers() {
        return managementMapper.findPunishList();
    }


    @Override
    public void getStatusUpdate(String memberNo, StatusCode statusCode) {
        if (statusCode == StatusCode.S010101 || statusCode == StatusCode.S010102 || statusCode == StatusCode.S010103 || statusCode == StatusCode.S010104) {
            managementMapper.updateStatus(memberNo, statusCode);
        }
    }

    @Override
    public void getPeriodupdate(MemberPunish memberPunish) {
        managementMapper.updatePunish(memberPunish);
    }

    @Override
    public void getPunishInsert(MemberPunish memberPunish) {
        managementMapper.insertPunish(memberPunish);
    }
}
