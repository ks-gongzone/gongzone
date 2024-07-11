package com.gongzone.central.party.after.domain;

import lombok.Data;

@Data
public class PartyPurchase {
	private String purchaseNo;
	private String partyNo;
	private String purchaseMemberNo;
	private String purchasePrice;
	private String status;

}
