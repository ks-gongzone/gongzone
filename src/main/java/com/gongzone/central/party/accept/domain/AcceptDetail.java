package com.gongzone.central.party.accept.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AcceptDetail {
    private String boardTitle;
    private String partyCateCode;
    private String productUrl;
    private String boardBody;
    private String partyAmount;
    private String remainAmount;
    private String partyPrice;
    private String remainPrice;
    private String boardNo;
    private String boardStatus;
    private Date endDate;
    private String status;
    private String address;
    private String thumbnail;       // 이미지 파일
    private String memberNo;
    private List<AcceptMember> participants;    // 이게 null 값
    private String partyNo;         // 진짜 partyNo
    private String partyLeader;
    private List<RequestMember> requestMember;
    private String img;
}
