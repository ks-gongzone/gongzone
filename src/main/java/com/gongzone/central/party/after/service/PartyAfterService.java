package com.gongzone.central.party.after.service;

import com.gongzone.central.party.after.domain.Reception;
import com.gongzone.central.party.after.domain.Shipping;
import com.gongzone.central.point.domain.request.PointRequest;

public interface PartyAfterService {

	void purchase(String partyNo, String memberNo, PointRequest partyPurchaseRequest);

	void updateShipping(String partyNo, String shippingNo, Shipping shipping);

	void updateShippingComplete(String partyNo, String shippingNo);

	void updateReceptionComplete(String partyNo, String receptionNo, Reception reception);


	// Below for test
	void testInsertParty();

}
