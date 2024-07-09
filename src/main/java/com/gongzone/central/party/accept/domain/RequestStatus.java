package com.gongzone.central.party.accept.domain;

import lombok.Data;

@Data
public class RequestStatus {
    private String partyId;
    private String partyNo;
    private String statusCode;
    private int requestAmount;
}
