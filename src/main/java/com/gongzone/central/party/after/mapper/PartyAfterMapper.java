package com.gongzone.central.party.after.mapper;

import com.gongzone.central.party.after.domain.PartyPurchaseDetail;
import com.gongzone.central.party.after.domain.Reception;
import com.gongzone.central.party.after.domain.Settlement;
import com.gongzone.central.party.after.domain.SettlementDetail;
import com.gongzone.central.party.after.domain.Shipping;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PartyAfterMapper {

	void insertPurchaseDetail(PartyPurchaseDetail detail);

	void updatePurchaseComplete(String purchaseNo);

	boolean checkPurchaseComplete(String partyNo);

	void insertShipping(String partyNo);

	void updateShipping(Shipping shipping);

	List<String> getPartyMembers(String partyNo);

	List<String> getPartyMembersExcludeLeader(String partyNo);

	String getLastIdxReception();

	void insertReception(@Param("partyNo") String partyNo,
						 @Param("receptions") List<Reception> receptions);

	void updateReception(Reception reception);

	boolean checkReceptionComplete(String partyNo);

	int calculateSettlementPrice(String partyNo);

	void insertPartySettlement(Settlement settlement);

	void updateShippingStatus(@Param("partyNo") String partyNo,
							  @Param("statusCode") String statusCode);


	// Below for test
	void testInsertParty(@Param("partyNo") String partyNo,
						 @Param("boardNo") String boardNo);

	void testInsertBoard(String boardNo);

	void testInsertLocation(String boardNo);

	int testInsertFile(String boardNo);

	void testInsertFileRelation(@Param("fileNo") int fileNo,
								@Param("boardNo") String boardNo);

	void testInsertPartyMember(@Param("partyMemberNo") String partyMemberNo,
							   @Param("partyNo") String partyNo,
							   @Param("purchasePrice") String purchasePrice);

	String testGetLastIdxBoard();

	String testGetLastIdxParty();

	String testGetLastIdxPartyMember();

	void testChangeBoardStatus(@Param("boardNo") String boardNo,
							   @Param("status") String status);

	void testChangePartyStatus(@Param("partyNo") String partyNo,
							   @Param("status") String status);

	void testInsertPartyPurchase(@Param("partyNo") String partyNo,
								 @Param("partyMemberNo") String partyMemberNo,
								 @Param("purchasePrice") String purchasePrice);

	void insertSettlementDetail(SettlementDetail settlementDetail);

	int getSettlementPrice(String partyNo);

	String getLeaderPartyMemberNo(String partyNo);

	String getPartySettlementNo(String partyNo);

	void updateSettlementState(String settlementNo, String status);

	String getMemberNoByPartyMemberNo(String partyMemberNo);

	void updatePartyComplete(String partyNo);

}
