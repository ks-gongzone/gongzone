package com.gongzone.central.party.accept.mapper;

import com.gongzone.central.party.accept.domain.*;
import com.gongzone.central.utils.StatusCode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AcceptMapper {
    AcceptDetail getPartyDetail(String partyNo);
    List<AcceptMember> getParticipants(String partyNo);
    List<RequestMember> getRequestMember(String partyNo);

    List<String> findPointNoByMemberNo(String memberNo);
    void updatePartyStatus(String partyId, StatusCode statusCode);
    // void deletePartyStatus(String partyId);
    void insertPartyMember(RequestParty requestParty);
    RequestParty requestMemberByPartyId(String partyId);
    void updateAmountMember(String partyNo);
}
