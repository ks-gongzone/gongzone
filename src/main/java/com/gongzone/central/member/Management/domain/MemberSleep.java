package com.gongzone.central.member.Management.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class MemberSleep {
    private final String memberNo;
    private final int memberLevel;
    private final String memberId;
    private final String memberPw;
    private final String memberName;
    private final String memberEmail;
    private final String memberPhone;
    private final String memberGender;
    private final String memberAddress;
    private final LocalDate memberBirthday;
    private final String memberNick;
    private final String memberStatus;
    private int memberSleepNo;
    private Timestamp memberLastLogin;
    private Timestamp memberSleepDate;
}
