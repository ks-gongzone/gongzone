package com.gongzone.central.party.accept.service;

import com.gongzone.central.party.accept.domain.AcceptDetail;
import com.gongzone.central.party.accept.domain.AcceptMember;
import com.gongzone.central.party.accept.mapper.AcceptMapper;
import com.gongzone.central.party.accept.service.AcceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcceptServiceImpl implements AcceptService {

    private final AcceptMapper acceptMapper;

    @Autowired
    public AcceptServiceImpl(AcceptMapper acceptMapper) {
        this.acceptMapper = acceptMapper;
    }

    @Override
    public AcceptDetail getPartyDetail(String partyId) {
        AcceptDetail detail = acceptMapper.getPartyDetail(partyId);
        List<AcceptMember> participants = acceptMapper.getParticipants(partyId);
        detail.setParticipants(participants);
        return detail;
    }

    @Override
    public List<AcceptMember> getParticipants(String partyId) {
        return acceptMapper.getParticipants(partyId);
    }
}
