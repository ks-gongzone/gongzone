package com.gongzone.central.party.accept.controller;

import com.gongzone.central.party.accept.domain.AcceptDetail;
import com.gongzone.central.party.accept.domain.AcceptMember;
import com.gongzone.central.party.accept.service.AcceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/party")
public class AcceptController {

    private final AcceptService acceptService;

    @Autowired
    public AcceptController(AcceptService acceptService) {
        this.acceptService = acceptService;
    }

    @GetMapping("/accept/{partyId}")
    public AcceptDetail getPartyDetail(@PathVariable String partyId) {
        System.out.println("Received request for party ID: " + partyId);
        AcceptDetail detail = acceptService.getPartyDetail(partyId);
        System.out.println("Returning detail: " + detail);
        return detail;
    }

    @GetMapping("/accept/{partyId}/participants")
    public List<AcceptMember> getParticipants(@PathVariable String partyId) {
        return acceptService.getParticipants(partyId);
    }
}
