package com.gongzone.central.party.accept.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
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