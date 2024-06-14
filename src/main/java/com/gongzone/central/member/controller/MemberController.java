package com.gongzone.central.member.controller;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/member")
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

    @PostMapping
    public ResponseEntity<Member> login(@RequestBody Member member) {
        return null;
    }

    public ResponseEntity<Member> findMemberById(@RequestBody Member member) {
        return null;
    }

    public ResponseEntity<List<Member>> findAllMembers() {
        return null;
    }

}
