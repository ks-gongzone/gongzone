package com.gongzone.central.party.accept.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AcceptMember {
    private String memberNo;
    private String memberNick;
    private String memberAmount;
    private String memberEmail;
    private String partyNo;
    private int requestPrice;
    private String partyMemberNo;
}
