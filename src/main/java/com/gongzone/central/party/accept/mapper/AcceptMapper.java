package com.gongzone.central.party.accept.mapper;

import com.gongzone.central.party.accept.domain.AcceptDetail;
import com.gongzone.central.party.accept.domain.AcceptMember;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AcceptMapper {
    AcceptDetail getPartyDetail(String partyId);
    List<AcceptMember> getParticipants(String PartyId);
}
