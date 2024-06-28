package com.gongzone.central.member.myInfo.alert.service;

import com.gongzone.central.member.myInfo.alert.domain.MyAlert;

public interface AlertService {
    MyAlert getAlertsByMemberNo(String memberNo);
    MyAlert updateAlertSettings(MyAlert myAlert);
}
