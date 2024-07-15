package com.gongzone.central.party.after.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SettlementDetail {

	private String settleDetailNo;
	private String partySettleNo;
	private String pointHistoryNo;
	private int partySettlePrice;
	private String partySettleDate;

}
