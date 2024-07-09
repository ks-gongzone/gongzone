package com.gongzone.central.party.accept.domain;

import lombok.Data;

import java.util.Date;

@Data
public class RequestParty {
    private String partyMemberNo;
    private int requestNo;
    private String partyNo;
    private String memberNo;
    private int requestAmount;
    private int requestPrice;
    private Date requestDate;
    private String statusCode;
}
