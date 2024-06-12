package com.gongzone.central.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member {
    private String memberNo;
    private int memberLevel;
    private String memberId;
    private String memberPw;
    private String memberName;
    private String memberEmail;
    private String memberPhone;
    private String memberGender;
    private String memberAddress;
    private String memberBirthday;
    private String memberNick;
    private String memberStatus;
}
