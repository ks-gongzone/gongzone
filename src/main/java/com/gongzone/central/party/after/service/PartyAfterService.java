package com.gongzone.central.party.after.service;

import com.gongzone.central.point.domain.request.PointRequest;

public interface PartyAfterService {

	void purchase(String partyNo, String memberNo, PointRequest partyPurchaseRequest);


	// Below for test
	void testInsertParty();

}
