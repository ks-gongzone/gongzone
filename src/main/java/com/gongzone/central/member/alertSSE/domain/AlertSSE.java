package com.gongzone.central.member.alertSSE.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AlertSSE {
    private int alertNo;                // 고유번호
    private String memberNo;            // 수신회원
    private String typeCode;            // 유형
    private String alertDetail;         // 상세메세지
    private Timestamp alertUpTime;      // 보낸시간
    private Timestamp alertReadTime;    // 읽은시간
    private String statusCode;          // 상태
}
