package com.gongzone.central.party.accept.service;

import com.gongzone.central.board.domain.BoardReply;
import com.gongzone.central.board.mapper.BoardMapper;
import com.gongzone.central.member.alertSSE.domain.AlertSSE;
import com.gongzone.central.member.alertSSE.service.AlertSSEService;
import com.gongzone.central.member.mapper.MemberMapper;
import com.gongzone.central.party.accept.domain.*;
import com.gongzone.central.party.accept.mapper.AcceptMapper;
import com.gongzone.central.utils.MySqlUtil;
import com.gongzone.central.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AcceptServiceImpl implements AcceptService {

    private final AcceptMapper acceptMapper;
    private final BoardMapper boardMapper;
    private final MemberMapper memberMapper;
    private final AlertSSEService alertSSEService;

    @Override
    public PartyMemberPurchase getPurchaseInfo(String memberNo, String partyNo) {
        return acceptMapper.getPartyMemberPurchase(memberNo, partyNo);
    }

    @Override
    public List<AcceptDetail> getPartyList(String partyNo) {
        AcceptDetail detail = acceptMapper.getPartyList(partyNo);

        String partyNos = detail.getPartyNo();
        List<AcceptMember> participants = acceptMapper.getParticipants(partyNos);
        detail.setParticipants(participants);

        List<RequestMember> requestMember = acceptMapper.getRequestMember(partyNos);
        detail.setRequestMember(requestMember);

        List<AcceptDetail> details = new ArrayList<>();
        details.add(detail);
        return details;
    }

    @Override
    public AcceptDetail getPartyDetailByPartyNo(String partyNo) {
        AcceptDetail detail = acceptMapper.getPartyDetail(partyNo);

        String partyNos = detail.getPartyNo();
        List<AcceptMember> participants = acceptMapper.getParticipants(partyNos);
        detail.setParticipants(participants);

        List<RequestMember> requestMember = acceptMapper.getRequestMember(partyNos);
        detail.setRequestMember(requestMember);

        String boardNos = detail.getBoardNo();
        List<BoardReply> boardReply = boardMapper.getBoardReplyList(boardNos);
        for (int i = 0; i < boardReply.size(); i++) {
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
        List<AcceptDetail> details = new ArrayList<>();

        for (String partyNo : partyNos) {
            List<AcceptDetail> detail = getPartyList(partyNo);
            details.addAll(detail);
        }

        return details;
    }

    @Override
    public Mono<Void> completeParty(String partyNo) {
        AcceptDetail detail = acceptMapper.getPartyList(partyNo);
        String partyNos = detail.getPartyNo();
        List<AcceptMember> participants = acceptMapper.getParticipants(partyNos);

        // 안전장치 추가: remainAmount가 0이 아닌 경우 메서드 종료
        if (Integer.parseInt(detail.getRemainAmount()) != 0) {
            return Mono.empty();
        }
        acceptMapper.completeBoardStatus(detail.getBoardNo());
        acceptMapper.completePartyStatus(detail.getPartyNo());

        String leaderNo = detail.getPartyLeader();
        for (AcceptMember member : participants) {

            // 확인후 주석 제거해주세요; 0716 우석
            // 파티장 결제완료 상태로 넣는 것에서 -> 파티장은 결제현황에 넣지 않도록 수정
            if (member.getMemberNo().equals(leaderNo)) {
                continue;
            }

            acceptMapper.insertPartyPurchase(detail.getPartyNo(), member.getPartyMemberNo(), member.getRequestPrice());
        }

        return Flux.fromIterable(participants)
                .flatMap(participant -> completeAlert(participant.getMemberNo()))
                .then();
    }

    @Override
    public Mono<Void> getPartyStatusByNo(String memberNo, String partyNo, StatusCode statusCode, int requestAmount) {
        if (statusCode == StatusCode.REFUSE || statusCode == StatusCode.CANCEL) {

            RequestParty requestParty = acceptMapper.requestMemberBymemberNo(memberNo, partyNo);

            acceptMapper.deletePartyRequest(memberNo, partyNo);
            return Mono.empty();
        } else if (statusCode == StatusCode.ACCEPT) {
            acceptMapper.updatePartyStatus(memberNo, statusCode);

            RequestParty requestParty = acceptMapper.requestMemberBymemberNo(memberNo, partyNo);

            String lastPartyMemberNo = acceptMapper.lastPartyMemberNo();
            String newLastPartyMemberNo = MySqlUtil.generatePrimaryKey(lastPartyMemberNo);

            requestParty.setPartyMemberNo(newLastPartyMemberNo);

            acceptMapper.insertPartyMember(requestParty);
            acceptMapper.updateAmountMember(requestParty);

            // 알림을 전송
            return acceptAlert(memberNo);
        } else if (statusCode == StatusCode.KICK) {
            RequestParty requestParty = acceptMapper.requestMemberBymemberNo(memberNo, partyNo);

            int unitPrice = acceptMapper.getPartyUnitPrice(partyNo);
            requestParty.setRequestPrice(requestParty.getRequestAmount() * unitPrice);

            acceptMapper.kickPartyMember(requestParty);

            acceptMapper.deletePartyRequest(memberNo, partyNo);

            acceptMapper.updateAmountAfterKick(requestParty);
            return kickAlert(memberNo);
        } else if (statusCode == StatusCode.REQUEST) {
            RequestParty requestParty = new RequestParty();
            requestParty.setPartyNo(partyNo);
            requestParty.setMemberNo(memberNo);
            requestParty.setRequestAmount(requestAmount);

            acceptMapper.requestJoin(requestParty);


            String leaderNo = acceptMapper.getPartyLeaderByPartyNo(partyNo);
            // memberNo 대신 leaderNo == 결국 memberNo
            return requestAlert(leaderNo);
        }
        return Mono.empty();
    }


    @Override
    public RequestParty getRequestMemberBymemberNo(String memberNo, String partyNo) {
        return acceptMapper.requestMemberBymemberNo(memberNo, partyNo);
    }

    private Mono<Void> acceptAlert(String memberNo) {
        return Mono.fromCallable(() -> {
                    AlertSSE alertSSE = new AlertSSE();
                    alertSSE.setMemberNo(memberNo);
                    alertSSE.setTypeCode("T010206"); // 알림 유형 코드
                    alertSSE.setAlertDetail("파티에 수락되었습니다.");
                    return alertSSE;
                })
                .flatMap(alertSSE -> alertSSEService.sendAlert(alertSSE))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnError(e -> {
                    e.printStackTrace();
                })
                .then(); // sendAlert의 반환 타입을 Mono<Void>로 변경
    }

    private Mono<Void> requestAlert(String memberNo) {
        return Mono.fromCallable(() -> {
                    AlertSSE alertSSE = new AlertSSE();
                    alertSSE.setMemberNo(memberNo);
                    alertSSE.setTypeCode("T010206"); // 알림 유형 코드
                    alertSSE.setAlertDetail("파티 신청이 있습니다.");
                    return alertSSE;
                })
                .flatMap(alertSSE -> alertSSEService.sendAlert(alertSSE)) // 단일 줄로 변경
                .subscribeOn(Schedulers.boundedElastic())
                .doOnError(e -> {
                    e.printStackTrace();
                })
                .then(); // sendAlert의 반환 타입을 Mono<Void>로 변경
    }

    private Mono<Void> kickAlert(String memberNo) {
        return Mono.fromCallable(() -> {
                    AlertSSE alertSSE = new AlertSSE();
                    alertSSE.setMemberNo(memberNo);
                    alertSSE.setTypeCode("T010206"); // 알림 유형 코드
                    alertSSE.setAlertDetail("파티에서 강퇴되었습니다.");
                    return alertSSE;
                })
                .flatMap(alertSSE -> alertSSEService.sendAlert(alertSSE)) // 단일 줄로 변경
                .subscribeOn(Schedulers.boundedElastic())
                .doOnError(e -> {
                    e.printStackTrace();
                })
                .then(); // sendAlert의 반환 타입을 Mono<Void>로 변경
    }

    private Mono<Void> completeAlert(String memberNo) {
        return Mono.fromCallable(() -> {
                    AlertSSE alertSSE = new AlertSSE();
                    alertSSE.setMemberNo(memberNo);
                    alertSSE.setTypeCode("T010206"); // 알림 유형 코드
                    alertSSE.setAlertDetail("파티 모집이 완료되었습니다. 결제를 진행해주세요!");
                    return alertSSE;
                })
                .flatMap(alertSSE -> alertSSEService.sendAlert(alertSSE)) // 단일 줄로 변경
                .subscribeOn(Schedulers.boundedElastic())
                .doOnError(e -> {
                    e.printStackTrace();
                })
                .then(); // sendAlert의 반환 타입을 Mono<Void>로 변경
    }
    //    @Override
    //    public void deletePartyStatusByNo(String memberNo) {
    //        acceptMapper.deletePartyStatus(memberNo);
    //    }
}
