package com.gongzone.central.member.note.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Note {
    private int noteNo;                 // 고유번호
    private String memberId;
    private String memberNo;            // 보낸회원
    private String memberTargetNo;      // 수신회원
    private String noteBody;            // 내용
    private Timestamp noteSendTime;     // 보낸일시
    private Timestamp noteReadTime;     // 읽은일시
    private String statusCode;          // 상태
    private String statusCodeTarget;    // 수신상태
}
