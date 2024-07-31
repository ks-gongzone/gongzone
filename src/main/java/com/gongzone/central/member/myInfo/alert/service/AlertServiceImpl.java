package com.gongzone.central.member.myInfo.alert.service;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.myInfo.alert.domain.MyAlert;
import com.gongzone.central.member.myInfo.alert.mapper.AlertMapper;
import com.gongzone.central.member.myInfo.service.MyInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @제목: 알람 설정 변경 및 히스토리 추가로직
 * @작성일: 2024-06-26
 * @수정일: 2024-07-01
 * @내용: 기존값을 갖지 않은 회원에 대한 데이터 처리 로직 추가
 */
@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private final AlertMapper alertMapper;
    private final MyInfoService myInfoService;

    @Override
    public MyAlert getAlertsByMemberNo(String memberNo) {
        MyAlert alert = alertMapper.findAlertByMemberNo(memberNo);
        if (alert == null) {
            alert = createDefaultAlert(memberNo);
        }
        return alert;
    }

    @Override
    public MyAlert updateAlertSettings(MyAlert myAlert) {
        try {
            MyAlert existAlert = alertMapper.findAlertByMemberNo(myAlert.getMemberNo());
            setDefaultAlertValues(myAlert);
            if (existAlert == null) {
                alertMapper.insertAlertSettings(myAlert);
            } else {
                myAlert.setAlertAllowNo(existAlert.getAlertAllowNo());
                alertMapper.updateAlertSettings(myAlert);
            }
            return alertMapper.findAlertByMemberNo(myAlert.getMemberNo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void insertAlertSettings(MyAlert myAlert) {
        MyAlert existingAlert = alertMapper.findAlertByMemberNo(myAlert.getMemberNo());
        if (existingAlert != null) {
            throw new IllegalArgumentException("기존 알람 설정이 존재합니다.");
        }

        Member member = myInfoService.findByNo(myAlert.getMemberNo());
        if (member == null) {
            throw new IllegalArgumentException("회원정보가 일치하지 않습니다.");
        }
        setDefaultAlertValues(myAlert);
        alertMapper.insertAlertSettings(myAlert);
    }

    @Override
    public MyAlert createDefaultAlert(String memberNo) {
        Member member = myInfoService.findByNo(memberNo);
        if (member != null) {
            Map<String, Object> defaultAlertSettings = new HashMap<>();
            defaultAlertSettings.put("smsAlert", false);
            defaultAlertSettings.put("emailAlert", false);
            defaultAlertSettings.put("marketingAlert", false);
            defaultAlertSettings.put("memberAlert", false);
            defaultAlertSettings.put("noteAlert", false);
            defaultAlertSettings.put("bulletinAlert", false);
            defaultAlertSettings.put("partyAlert", false);

            return new MyAlert().useMyAlert(member.getMemberNo(), defaultAlertSettings);
        } else {
            throw new IllegalArgumentException("해당 유저가 없습니다: " + memberNo);
        }
    }

    @Override
    public void setDefaultAlertValues(MyAlert myAlert) {
        if (myAlert.getSmsAlert() == null) myAlert.setSmsAlert(false);
        if (myAlert.getEmailAlert() == null) myAlert.setEmailAlert(false);
        if (myAlert.getMarketingAlert() == null) myAlert.setMarketingAlert(false);
        if (myAlert.getMemberAlert() == null) myAlert.setMemberAlert(false);
        if (myAlert.getNoteAlert() == null) myAlert.setNoteAlert(false);
        if (myAlert.getBulletinAlert() == null) myAlert.setBulletinAlert(false);
        if (myAlert.getPartyAlert() == null) myAlert.setPartyAlert(false);
    }
}