package com.gongzone.central.party.accept.service;

import com.gongzone.central.party.accept.domain.AcceptDetail;
import com.gongzone.central.party.accept.domain.AcceptMember;
import com.gongzone.central.party.accept.domain.AddedMember;
import com.gongzone.central.party.accept.domain.RequestMember;
import com.gongzone.central.utils.StatusCode;

import java.util.List;

public interface AcceptService {
    List<AcceptDetail> getPartyDetail(String partyNo);
    List<AcceptMember> getParticipants(String partyNo);

    List<RequestMember> getRequestMember(String partyNo);

    List<AcceptDetail> getListParty(String memberNo);

    List<String> getPartyNo(String memberNo);
    void getPartyStatusByNo(String partyId, StatusCode statusCode);
//    void deletePartyStatusByNo(String partyId);

}
