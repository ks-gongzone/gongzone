package com.gongzone.central.party.after.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Settlement {

	private int partySettleNo;
	private String partyNo;
	private String partyMemberNo;
	private int partySettlePrice;
	private String status;

	public Settlement(String partyNo, int partySettlePrice) {
		this.partyNo = partyNo;
		this.partySettlePrice = partySettlePrice;
	}

}
