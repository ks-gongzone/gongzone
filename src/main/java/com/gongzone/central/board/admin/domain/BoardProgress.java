package com.gongzone.central.board.admin.domain;

import lombok.Data;

@Data
public class BoardProgress {
    private String boardNo;
    private String partyNo;
    private String partyStatus;
    private int totalPartyMember;
    private int purchaseCompleteMember;
    private int receptionCompleteMember;
}
