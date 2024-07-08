package com.gongzone.central.party.accept.service;

import com.gongzone.central.party.accept.domain.*;
import com.gongzone.central.party.accept.mapper.AcceptMapper;
import com.gongzone.central.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AcceptServiceImpl implements AcceptService {

    private final AcceptMapper acceptMapper;

    @Override
    public List<AcceptDetail> getPartyDetail(String partyNo) {
        AcceptDetail detail = acceptMapper.getPartyDetail(partyNo);
        System.out.println("detail  : " + detail);

        List<AcceptMember> participants = acceptMapper.getParticipants(partyNo);
        System.out.println("participants : " + participants);
        detail.setParticipants(participants);

        List<RequestMember> requestMember = acceptMapper.getRequestMember(partyNo);
        System.out.println("requestMember : " + requestMember);
        detail.setRequestMember(requestMember);

        List<AcceptDetail> details = new ArrayList<>();
        details.add(detail);
        return details;
    }

    @Override
    public List<AcceptMember> getParticipants(String partyNo) {
        return acceptMapper.getParticipants(partyNo);
    }

    @Override
    public List<String> getPartyNo(String memberNo) {
        return acceptMapper.findPointNoByMemberNo(memberNo);
    }

    @Override
    public List<RequestMember> getRequestMember(String partyNo) {
        return acceptMapper.getRequestMember(partyNo);
    }

    @Override
    public List<AcceptDetail> getListParty(String memberNo) {
        List<String> partyNos = getPartyNo(memberNo);
        List<AcceptDetail> details = new ArrayList<>();

        for (String partyNo : partyNos) {
            List<AcceptDetail> detail = getPartyDetail(partyNo);
            details.addAll(detail);
        }
        return details;
    }

    @Override
    public void getPartyStatusByNo(String partyId, StatusCode statusCode) {
        if (statusCode == StatusCode.REFUSE || statusCode == StatusCode.CANCEL || statusCode == StatusCode.KICK) {
            System.out.println("삭제 실행");
            acceptMapper.updatePartyStatus(partyId, statusCode);
//            deletePartyStatusByNo(partyId);
        } else if (statusCode == StatusCode.ACCEPT){
            System.out.println("업데이트 실행");
            acceptMapper.updatePartyStatus(partyId, statusCode);
            RequestParty requestParty = acceptMapper.requestMemberByPartyId(partyId);
            System.out.println("requestParty : " + requestParty);
            acceptMapper.insertPartyMember(requestParty);
            System.out.println("requestParty.getPartyNo() : " + requestParty.getPartyNo());
            acceptMapper.updateAmountMember(requestParty.getPartyNo());
        }
    }



//    @Override
//    public void deletePartyStatusByNo(String partyId) {
//        acceptMapper.deletePartyStatus(partyId);
//    }
}
