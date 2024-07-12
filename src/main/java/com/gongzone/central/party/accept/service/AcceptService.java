package com.gongzone.central.party.accept.service;

import com.gongzone.central.party.accept.domain.*;
import com.gongzone.central.utils.StatusCode;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AcceptService {
    List<AcceptDetail> getPartyList(String partyNo);
    List<AcceptMember> getParticipants(String partyNo);

    List<RequestMember> getRequestMember(String partyNo);

    List<AcceptDetail> getListParty(String memberNo);

    List<String> getPartyNo(String memberNo);
    Mono<Void> getPartyStatusByNo(String memberNo, String partyNo, StatusCode statusCode, int requestAmount);
    RequestParty getRequestMemberBymemberNo(String memberNo, String partyNo);
    AcceptDetail getPartyDetailByPartyNo(String partyNo);
    Mono<Void> completeParty(String partyNo);
    PartyMemberPurchase getPurchaseInfo(String memberNo, String partyNo);
//    void deletePartyStatusByNo(String memberNo);

}
