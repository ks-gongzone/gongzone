package com.gongzone.central.party.accept.service;

import com.gongzone.central.party.accept.domain.AcceptDetail;
import com.gongzone.central.party.accept.domain.AcceptMember;
import com.gongzone.central.party.accept.mapper.AcceptMapper;
import com.gongzone.central.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AcceptServiceImpl implements AcceptService {

    private final AcceptMapper acceptMapper;

    @Override
    public AcceptDetail getPartyDetail(String partyNo) {
        AcceptDetail detail = acceptMapper.getPartyDetail(partyNo);
        System.out.println("detail  : " + detail);
        List<AcceptMember> participants = acceptMapper.getParticipants(partyNo);
        System.out.println("participants : " + participants);
        detail.setParticipants(participants);
        return detail;
    }

    @Override
    public List<AcceptMember> getParticipants(String partyNo) {
        return acceptMapper.getParticipants(partyNo);
    }

    @Override
    public String getPartyNo(String partyId) {
        return acceptMapper.findPointNoByMemberNo(partyId);
    }

    @Override
    public void getPartyStatusByNo(String partyId, StatusCode statusCode) {
        if (statusCode == StatusCode.REFUSE || statusCode == StatusCode.CANCEL || statusCode == StatusCode.KICK) {
            System.out.println("삭제 실행");
            // 업데이트 해주고 삭제 (삭제할건지 아니면 업데이트로해서 기록에 남겨둘건지 내일 상의후 결정)
            deletePartyStatusByNo(partyId);
        } else {
            System.out.println("업데이트 실행");
            acceptMapper.updatePartyStatus(partyId, statusCode);
        }
    }

    @Override
    public void deletePartyStatusByNo(String partyId) {
        acceptMapper.deletePartyStatus(partyId);
    }
}
