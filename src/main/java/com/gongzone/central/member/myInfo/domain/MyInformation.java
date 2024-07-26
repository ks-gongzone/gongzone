package com.gongzone.central.member.myInfo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @제목: 내정보 설정
 * @생성일: 2024-06-18
 * @수정일: 2024-06-18
 * @내용: 변경사항들에 대한 값이므로 final선언 안했지만 필요에 따라 선언 가능
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyInformation {
    private String newPassword; // null 가능
    private String newMemberNick; // null 가능
    private String newMemberAddress; // null 가능
    
    private String statusCode;
    private String newStatusCode;
}
