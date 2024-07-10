package com.gongzone.central.member.Management.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberPunish {
    private String memberNo;
    private int memberLevel;
    private String memberId;
    private String memberPw;
    private String memberName;
    private String memberEmail;
    private String memberPhone;
    private String memberGender;
    private String memberAddress;
    private LocalDate memberBirthday;
    private String memberNick;
    private String memberStatus;
    private int memberPunishNo;
    private String memberAdminNo;
    private String punishType;
    private String punishReason;
    private Timestamp punishStartDate;
    private String punishPeriod;
    private Timestamp punishEndPeriod;
    private String punishStatus;
}
