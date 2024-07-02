package com.gongzone.central.member.myInfo.alert.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 개별 알람 설정
 * @date: 2024-06-18
 * @수정일: 2024-07-01
 * @수정내용: null값 허용을 위해 타입 변경
 */
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class MyAlert {
    private int alertAllowNo;
    private String memberNo;
    // null 값 허용하기 위해 원시타입에서 참조타입 변경
    private Boolean smsAlert;
    private Boolean emailAlert;
    private Boolean marketingAlert;
    private Boolean memberAlert;
    private Boolean noteAlert;
    private Boolean bulletinAlert;
    private Boolean partyAlert;

    // MyAlert 객체를 생성하는 메서드 추가
    public MyAlert useMyAlert(String memberNo, Map<String, Object> alertData) {
        this.memberNo = memberNo;
        this.smsAlert = (Boolean) alertData.get("smsAlert");
        this.emailAlert = (Boolean) alertData.get("emailAlert");
        this.marketingAlert = (Boolean) alertData.get("marketingAlert");
        this.memberAlert = (Boolean) alertData.get("memberAlert");
        this.noteAlert = (Boolean) alertData.get("noteAlert");
        this.bulletinAlert = (Boolean) alertData.get("bulletinAlert");
        this.partyAlert = (Boolean) alertData.get("partyAlert");
        return this;
    }

}
