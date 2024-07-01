package com.gongzone.central.member.myInfo.alert.service;

import com.gongzone.central.member.myInfo.alert.mapper.AlertMapper;
import com.gongzone.central.member.myInfo.alert.domain.MyAlert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @제목: 알람 설정 변경 및 히스토리 추가로직
 * @작성일: 2024-06-26
 * @수정일: 2024-06-26
 */
@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {
    private final AlertMapper alertMapper;

    @Override
    public MyAlert getAlertsByMemberNo(String memberNo) {
        System.out.println("서비스 알람 MEMBER NO: " + memberNo);
        MyAlert alert = alertMapper.findAlertByMemberNo(memberNo);
        if (alert == null) {
            throw new IllegalArgumentException("해당 유저가 없습니다: " + memberNo);
        }
        return alert;
    }

    @Override

    public MyAlert updateAlertSettings(MyAlert myAlert) {
        try {
            alertMapper.updateAlertSettings(myAlert);
            return alertMapper.findAlertByMemberNo(myAlert.getMemberNo());
        } catch (Exception e) {
            return null;
        }
    }
}