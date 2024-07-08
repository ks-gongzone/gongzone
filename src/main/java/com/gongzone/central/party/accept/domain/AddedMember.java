package com.gongzone.central.party.accept.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddedMember {
    private String partyMemberNo;
    private String partyNo;
    private String memberNo;
    private int amount;
    private int price;
    private Date joinDate;
    private String leader;
}
