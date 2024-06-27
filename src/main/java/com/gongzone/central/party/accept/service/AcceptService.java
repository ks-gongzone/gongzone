package com.gongzone.central.party.accept.service;

import com.gongzone.central.party.accept.domain.AcceptDetail;
import com.gongzone.central.party.accept.domain.AcceptMember;

import java.util.List;

public interface AcceptService {
    AcceptDetail getPartyDetail(String partyId);
    List<AcceptMember> getParticipants(String partyId);
}
