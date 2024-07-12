package com.gongzone.central.party.after.domain;

import lombok.Data;

@Data
public class Reception {

	private String receptionNo;
	private String partyNo;
	private String partyMemberNo;
	private String receptionComment;
	private String receptionDate;
	private String statusCode;

	public Reception(String partyMemberNo) {
		this.partyMemberNo = partyMemberNo;
	}

}
