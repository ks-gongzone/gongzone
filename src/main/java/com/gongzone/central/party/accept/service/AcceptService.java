package com.gongzone.central.party.accept.service;

import com.gongzone.central.party.accept.domain.*;
import com.gongzone.central.utils.StatusCode;

import java.util.List;

public interface AcceptService {
    List<AcceptDetail> getPartyList(String partyNo);
    List<AcceptMember> getParticipants(String partyNo);

    List<RequestMember> getRequestMember(String partyNo);

    List<AcceptDetail> getListParty(String memberNo);

    List<String> getPartyNo(String memberNo);
    void getPartyStatusByNo(String partyId, String partyNo, StatusCode statusCode, int requestAmount);
    RequestParty getRequestMemberByPartyId(String partyId, String partyNo);
    AcceptDetail getPartyDetailByPartyNo(String partyNo);
    void completeParty(String partyNo);

//    void deletePartyStatusByNo(String partyId);

}
