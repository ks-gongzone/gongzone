package com.gongzone.central.party.after.mapper;

import com.gongzone.central.party.after.domain.PartyPurchaseDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PartyAfterMapper {

	void insertPurchaseDetail(PartyPurchaseDetail detail);

	void updatePurchaseComplete(String purchaseNo);


	// Below for test
	void testInsertParty(@Param("partyNo") String partyNo,
						 @Param("boardNo") String boardNo);

	void testInsertBoard(String boardNo);

	void testInsertLocation(String boardNo);

	int testInsertFile(String boardNo);

	void testInsertFileRelation(@Param("fileNo") int fileNo,
								@Param("boardNo") String boardNo);

	void testInsertPartyLeader(@Param("partyMemberNo") String partyMemberNo,
							   @Param("partyNo") String partyNo,
							   @Param("purchasePrice") String purchasePrice);

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

}
