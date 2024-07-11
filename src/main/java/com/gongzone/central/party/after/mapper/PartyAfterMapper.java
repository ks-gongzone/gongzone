package com.gongzone.central.party.after.mapper;

import com.gongzone.central.party.after.domain.PartyPurchaseDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PartyAfterMapper {

	void insertPurchaseDetail(PartyPurchaseDetail detail);

	void updatePurchaseSuccess(String purchaseNo);

}
