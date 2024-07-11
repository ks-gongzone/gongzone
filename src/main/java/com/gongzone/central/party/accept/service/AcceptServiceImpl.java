package com.gongzone.central.party.accept.service;

import com.gongzone.central.board.domain.BoardReply;
import com.gongzone.central.board.mapper.BoardMapper;
import com.gongzone.central.member.mapper.MemberMapper;
import com.gongzone.central.party.accept.domain.*;
import com.gongzone.central.party.accept.mapper.AcceptMapper;
import com.gongzone.central.utils.MySqlUtil;
import com.gongzone.central.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AcceptServiceImpl implements AcceptService {

    private final AcceptMapper acceptMapper;
    private final BoardMapper boardMapper;
    private final MemberMapper memberMapper;

    @Override
    public PartyMemberPurchase getPurchaseInfo(String memberNo, String partyNo) {
        return acceptMapper.getPartyMemberPurchase(memberNo, partyNo);
    }

    @Override
    public List<AcceptDetail> getPartyList(String partyNo) {
        AcceptDetail detail = acceptMapper.getPartyList(partyNo);
        System.out.println("detail1266  : " + detail);

        String partyNos = detail.getPartyNo();
        List<AcceptMember> participants = acceptMapper.getParticipants(partyNos);
        System.out.println("participants : " + participants);
        detail.setParticipants(participants);

        List<RequestMember> requestMember = acceptMapper.getRequestMember(partyNos);
        System.out.println("requestMember : " + requestMember);
        detail.setRequestMember(requestMember);

        List<AcceptDetail> details = new ArrayList<>();
        details.add(detail);
        return details;
    }

    @Override
    public AcceptDetail getPartyDetailByPartyNo(String partyNo) {
        AcceptDetail detail = acceptMapper.getPartyDetail(partyNo);
        System.out.println("detail1266  : " + detail);

        String partyNos = detail.getPartyNo();
        List<AcceptMember> participants = acceptMapper.getParticipants(partyNos);
        System.out.println("participants : " + participants);
        detail.setParticipants(participants);

        List<RequestMember> requestMember = acceptMapper.getRequestMember(partyNos);
        System.out.println("requestMember : " + requestMember);
        detail.setRequestMember(requestMember);

        String boardNos = detail.getBoardNo();
        List<BoardReply> boardReply = boardMapper.getBoardReplyList(boardNos);
        for (int i=0; i<boardReply.size(); i++) {
            String memberId = (memberMapper.info(boardReply.get(i).getMemberNo())).getMemberId();
            boardReply.get(i).setMemberId(memberId);
        }
        detail.setBoardReply(boardReply);

        return detail;
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
        System.out.println("partyNos :" + partyNos);
        List<AcceptDetail> details = new ArrayList<>();

        for (String partyNo : partyNos) {
            List<AcceptDetail> detail = getPartyList(partyNo);
            details.addAll(detail);
        }
        System.out.println("details on array : " + details);

        return details;
    }

    @Override
    public void completeParty(String partyNo) {
        AcceptDetail detail = acceptMapper.getPartyList(partyNo);
        String partyNos = detail.getPartyNo();
        List<AcceptMember> participants = acceptMapper.getParticipants(partyNos);

        // 안전장치 추가: remainAmount가 0이 아닌 경우 메서드 종료
        if (Integer.parseInt(detail.getRemainAmount()) != 0) {
            System.out.println("remainAmount가 0이 아니므로 completeParty 메서드를 종료합니다.");
            return;
        }
        System.out.println("detail in complete" + detail);
        acceptMapper.completeBoardStatus(detail.getBoardNo());
        acceptMapper.completePartyStatus(detail.getPartyNo());
        System.out.println("participants" + participants);
        for (AcceptMember member : participants) {
            acceptMapper.insertPartyPurchase(detail.getPartyNo(), member.getPartyMemberNo(), member.getRequestPrice());
        }
    }

    @Override
    public void getPartyStatusByNo(String partyId, String partyNo, StatusCode statusCode, int requestAmount) {
        if (statusCode == StatusCode.REFUSE || statusCode == StatusCode.CANCEL) {
            System.out.println("삭제 실행");

            RequestParty requestParty = acceptMapper.requestMemberByPartyId(partyId, partyNo);
            System.out.println("requestParty : " + requestParty);

            acceptMapper.deletePartyRequest(partyId, partyNo);
        } else if (statusCode == StatusCode.ACCEPT) {
            System.out.println("업데이트 실행");
            acceptMapper.updatePartyStatus(partyId, statusCode);


            RequestParty requestParty = acceptMapper.requestMemberByPartyId(partyId, partyNo);
            System.out.println("requestParty : " + requestParty);

            String lastPartyMemberNo = acceptMapper.lastPartyMemberNo();
            String newLastPartyMemberNo = MySqlUtil.generatePrimaryKey(lastPartyMemberNo);
            System.out.println(newLastPartyMemberNo);
            requestParty.setPartyMemberNo(newLastPartyMemberNo);

            acceptMapper.insertPartyMember(requestParty);
            System.out.println("requestParty.getPartyNo() : " + requestParty.getPartyNo());

            acceptMapper.updateAmountMember(requestParty);
        } else if (statusCode == StatusCode.KICK) {
            System.out.println("강퇴 실행");
            RequestParty requestParty = acceptMapper.requestMemberByPartyId(partyId, partyNo);
            System.out.println("requestParty : " + requestParty);

            int unitPrice = acceptMapper.getPartyUnitPrice(partyNo);
            requestParty.setRequestPrice(requestParty.getRequestAmount() * unitPrice);

            acceptMapper.kickPartyMember(requestParty);


            acceptMapper.deletePartyRequest(partyId, partyNo);


            acceptMapper.updateAmountAfterKick(requestParty);
        } else if (statusCode == StatusCode.REQUEST) {

            RequestParty requestParty = new RequestParty();
            requestParty.setPartyNo(partyNo);
            requestParty.setMemberNo(partyId);
            requestParty.setRequestAmount(requestAmount);
            System.out.println("requestParty in request :" + requestParty);

            acceptMapper.requestJoin(requestParty);

        }
    }

    @Override
    public RequestParty getRequestMemberByPartyId(String partyId, String partyNo) {
        return acceptMapper.requestMemberByPartyId(partyId, partyNo);
    }
//    @Override
//    public void deletePartyStatusByNo(String partyId) {
//        acceptMapper.deletePartyStatus(partyId);
//    }
}
