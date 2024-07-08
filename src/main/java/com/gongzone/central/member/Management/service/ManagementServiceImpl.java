package com.gongzone.central.member.Management.service;

import com.gongzone.central.member.Management.mapper.ManagementMapper;
import com.gongzone.central.member.domain.Member;
import com.gongzone.central.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagementServiceImpl implements ManagementService {

    private final ManagementMapper managementMapper;

    @Override
    public List<Member> getAllMembers() {
        return managementMapper.findAll();
    }

    @Override
    public List<Member> getQuitAllMembers() {
        return managementMapper.findQuitList();
    }

    @Override
    public List<Member> getSleepAllMembers() {
        return managementMapper.findSleepList();
    }

    @Override
    public List<Member> getPunishAllMembers() {
        return managementMapper.findPunishList();
    }


    @Override
    public void getStatusUpdate(String memberNo, StatusCode statusCode) {
        if (statusCode == StatusCode.S010101 || statusCode == StatusCode.S010102 || statusCode == StatusCode.S010103 || statusCode == StatusCode.S010104) {
            managementMapper.updateStatus(memberNo, statusCode);
        }
    }
}
