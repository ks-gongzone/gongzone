package com.gongzone.central.party.after.domain.request;

import com.gongzone.central.party.after.domain.PartyPurchaseDetail;
import lombok.Data;

@Data
public class PartyPurchaseRequest {
	private String memberPointNo;
	private PartyPurchaseDetail partyPurchaseDetail;

}
