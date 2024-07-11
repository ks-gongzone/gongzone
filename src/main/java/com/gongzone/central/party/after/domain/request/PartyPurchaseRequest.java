package com.gongzone.central.party.after.domain.request;

import com.gongzone.central.party.after.domain.PartyPurchaseDetail;
import com.gongzone.central.point.domain.request.PointRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class PartyPurchaseRequest extends PointRequest {

	private PartyPurchaseDetail partyPurchaseDetail;

}
