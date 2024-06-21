package com.gongzone.central.member.controller;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<Member> register(@RequestBody Member member) {
        Member registeredMember = memberService.registerMember(member);
        return ResponseEntity.ok(registeredMember);
    }

    @PostMapping("/check")
    public ResponseEntity<Boolean> findMemberById(@RequestBody String memberId) {
        Boolean checkId = memberService.getMemberById();
        return ResponseEntity.ok(checkId);
    }

    public ResponseEntity<List<Member>> findAllMembers() {
        return null;
    }

}
