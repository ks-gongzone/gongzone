package com.gongzone.central.party.after.service;

import com.gongzone.central.party.after.domain.request.PartyPurchaseRequest;

public interface PartyAfterService {

	void purchase(String partyNo, String memberNo, PartyPurchaseRequest partyPurchaseRequest);


	// Below for test
	void testInsertParty();

}
