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
    private Date endDate;
    private String status;
    private String address;
    private String thumbnail;
    private String partyId;
    private List<AcceptMember> participants;
}