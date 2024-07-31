package com.gongzone.central.party.accept.controller;

import com.gongzone.central.party.accept.domain.*;
import com.gongzone.central.party.accept.service.AcceptService;
import com.gongzone.central.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AcceptController {

    private final AcceptService acceptService;

    @GetMapping("/party/accept/{memberNo}")
    public List<AcceptDetail> getPartyList(@PathVariable String memberNo) {
        List<AcceptDetail> detail = acceptService.getListParty(memberNo);
        return detail;
    }

    @GetMapping("/party/detail/{partyNo}")
    public AcceptDetail getPartyDetail(@PathVariable String partyNo) {
        AcceptDetail detail = acceptService.getPartyDetailByPartyNo(partyNo);
        return detail;
    }

    @GetMapping("/party/accept/{memberNo}/participants")
    public List<AcceptMember> getParticipants(@PathVariable String memberNo) {
        return acceptService.getParticipants(memberNo);
    }

    @GetMapping("/party/purchase/{memberNo}/{partyNo}")
    public PartyMemberPurchase getPurchaseInfo(@PathVariable String memberNo, @PathVariable String partyNo) {
        return acceptService.getPurchaseInfo(memberNo, partyNo);
    }


    @GetMapping("/alertSSE/party/accept/{memberNo}/requestmember")
    public List<RequestMember> getRequestMember(@PathVariable String memberNo) {
        List<RequestMember> requestMember = acceptService.getRequestMember(memberNo);
        return requestMember;
    }

    @PostMapping("/alertSSE/party/accept/{memberNo}/Status")
    public Mono<ResponseEntity<String>> updateStatus(@PathVariable String memberNo, @RequestBody RequestStatus requestStatus) {
        try {
            StatusCode status = StatusCode.fromCode(requestStatus.getStatusCode());
            return acceptService.getPartyStatusByNo(memberNo, requestStatus.getPartyNo(), status, requestStatus.getRequestAmount())
                    .then(Mono.just(ResponseEntity.ok("Status updated to: " + status)))
                    .onErrorResume(IllegalArgumentException.class, e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status code: " + requestStatus.getStatusCode())));
        } catch (IllegalArgumentException e) {
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status code: " + requestStatus.getStatusCode()));
        }
    }

    @PostMapping("/alertSSE/party/updateStatus/{partyNo}")
    public Mono<ResponseEntity<String>> updatePartyAndBoardStatus(@PathVariable String partyNo) {
        return acceptService.completeParty(partyNo)
                .then(Mono.just(ResponseEntity.ok("Party and Board statuses updated successfully.")))
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the statuses."));
                });
    }
}
