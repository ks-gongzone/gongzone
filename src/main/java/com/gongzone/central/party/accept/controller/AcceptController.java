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
        System.out.println("Received request for party ID: " + memberNo);
        List<AcceptDetail> detail = acceptService.getListParty(memberNo);
        System.out.println("Returning detail: " + detail);
        return detail;
    }

    @GetMapping("/party/detail/{partyNo}")
    public AcceptDetail getPartyDetail(@PathVariable String partyNo) {
        System.out.println("Received request for party ID: " + partyNo);
        AcceptDetail detail = acceptService.getPartyDetailByPartyNo(partyNo);
        System.out.println("Returning detail: " + detail);
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


    @GetMapping("/party/accept/{memberNo}/requestmember")
    public List<RequestMember> getRequestMember(@PathVariable String memberNo) {
        List<RequestMember> requestMember = acceptService.getRequestMember(memberNo);
        System.out.println("Request members returned to client: " + requestMember);
        return requestMember;
    }

    @PostMapping("/alertSSE/party/accept/{memberNo}/Status")
    public Mono<ResponseEntity<String>> updateStatus(@PathVariable String memberNo, @RequestBody RequestStatus requestStatus) {
        System.out.println("1111111111111111111");
        System.out.println("1111111111111111111");
        System.out.println("1111111111111111111");
        try {
            StatusCode status = StatusCode.fromCode(requestStatus.getStatusCode());
            return acceptService.getPartyStatusByNo(memberNo, requestStatus.getPartyNo(), status, requestStatus.getRequestAmount())
                    .then(Mono.just(ResponseEntity.ok("Status updated to: " + status)))
                    .onErrorResume(IllegalArgumentException.class, e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status code: " + requestStatus.getStatusCode())));
        } catch (IllegalArgumentException e) {
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status code: " + requestStatus.getStatusCode()));
        }
    }




    @PostMapping("/party/updateStatus/{partyNo}")
    public ResponseEntity<String> updatePartyAndBoardStatus(@PathVariable String partyNo) {
        acceptService.completeParty(partyNo);
        return ResponseEntity.ok("Party and Board statuses updated successfully.");
    }


}
