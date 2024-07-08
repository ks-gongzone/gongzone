package com.gongzone.central.party.accept.controller;

import com.gongzone.central.party.accept.domain.AcceptDetail;
import com.gongzone.central.party.accept.domain.AcceptMember;
import com.gongzone.central.party.accept.domain.RequestMember;
import com.gongzone.central.party.accept.domain.RequestStatus;
import com.gongzone.central.party.accept.service.AcceptService;
import com.gongzone.central.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AcceptController {

    private final AcceptService acceptService;

    @GetMapping("/party/accept/{memberNo}")
    public List<AcceptDetail> getPartyDetail(@PathVariable String memberNo) {
        System.out.println("Received request for party ID: " + memberNo);
        List<AcceptDetail> detail = acceptService.getListParty(memberNo);
        System.out.println("Returning detail: " + detail);
        return detail;
    }

    @GetMapping("/party/accept/{partyId}/participants")
    public List<AcceptMember> getParticipants(@PathVariable String partyId) {
        return acceptService.getParticipants(partyId);
    }


    @GetMapping("/party/accept/{partyId}/requestmember")
    public List<RequestMember> getRequestMember(@PathVariable String partyId) {
        List<RequestMember> requestMember = acceptService.getRequestMember(partyId);
        System.out.println("Request members returned to client: " + requestMember);
        return requestMember;
    }

    @PostMapping("/party/accept/{partyId}/Status")
    public ResponseEntity<String> updateStatus(@PathVariable String partyId, @RequestBody RequestStatus requestStatus) {
        System.out.println("1111111111111111111");
        try {
            StatusCode status = StatusCode.fromCode(requestStatus.getStatusCode());
            acceptService.getPartyStatusByNo(partyId, status);
            return ResponseEntity.ok("Status updated to: " + status);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status code: " + requestStatus.getStatusCode());
        }
    }
}
