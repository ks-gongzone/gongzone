package com.gongzone.central.party.accept.mapper;

import com.gongzone.central.party.accept.domain.*;
import com.gongzone.central.utils.StatusCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AcceptMapper {
    AcceptDetail getPartyList(String partyNo);
    AcceptDetail getPartyDetail(String partyNo);
    List<AcceptMember> getParticipants(String partyNo);
    List<RequestMember> getRequestMember(String partyNo);


    List<String> findPointNoByMemberNo(String memberNo);
    void updatePartyStatus(String partyId, StatusCode statusCode);
    // void deletePartyStatus(String partyId);
    void insertPartyMember(RequestParty requestParty);
    RequestParty requestMemberByPartyId(@Param("partyId") String partyId, @Param("partyNo") String partyNo);
    void updateAmountMember(RequestParty requestParty);
    String lastPartyMemberNo();
    void kickPartyMember(RequestParty requestParty);
    void updateAmountAfterKick(RequestParty requestParty);
    void deletePartyRequest(@Param("partyId") String partyId, @Param("partyNo") String partyNo);
    int getPartyUnitPrice(String partyNo);
    void requestJoin(RequestParty requestParty);
    void completeBoardStatus(String boardNo);
    void completePartyStatus(String partyNo);
    void insertPartyPurchase(@Param("partyNo") String partyNo, @Param("partyMemberNo") String partyMemberNo, @Param("requestPrice") int requestPrice);
    PartyMemberPurchase getPartyMemberPurchase(String memberNo, String partyNo);
}
