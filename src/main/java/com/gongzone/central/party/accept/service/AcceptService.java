package com.gongzone.central.party.accept.service;

import com.gongzone.central.party.accept.domain.AcceptDetail;
import com.gongzone.central.party.accept.domain.AcceptMember;
import com.gongzone.central.utils.StatusCode;

import java.util.List;

public interface AcceptService {
    AcceptDetail getPartyDetail(String partyNo);
    List<AcceptMember> getParticipants(String partyNo);

    String getPartyNo(String partyId);
    void getPartyStatusByNo(String partyId, StatusCode statusCode);
    void deletePartyStatusByNo(String partyId);
}
