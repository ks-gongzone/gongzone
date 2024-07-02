package com.gongzone.central.party.accept.mapper;

import com.gongzone.central.party.accept.domain.AcceptDetail;
import com.gongzone.central.party.accept.domain.AcceptMember;
import com.gongzone.central.utils.StatusCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AcceptMapper {
    AcceptDetail getPartyDetail(String partyNo);
    List<AcceptMember> getParticipants(String partyNo);

    String findPointNoByMemberNo(@Param("partyId") String partyId);
    void updatePartyStatus(String partyId, StatusCode statusCode);
    void deletePartyStatus(String partyId);
}
