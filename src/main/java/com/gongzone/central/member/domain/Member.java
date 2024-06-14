package com.gongzone.central.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class Member {
    private final String memberNo;
    private final int memberLevel;
    private final String memberId;
    private final String memberPw;
    private final String memberName;
    private final String memberEmail;
    private final String memberPhone;
    private final String memberGender;
    private final String memberAddress;
    private final String memberBirthday;
    private final String memberNick;
    private final String memberStatus;
}
