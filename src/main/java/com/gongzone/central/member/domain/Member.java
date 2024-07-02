package com.gongzone.central.member.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongzone.central.utils.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private final LocalDate memberBirthday;
    private final String memberNick;
    private final StatusCode memberStatus;
}
