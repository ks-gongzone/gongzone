package com.gongzone.central.member.controller;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.login.security.JwtUtil;
import com.gongzone.central.member.login.service.MemberDetails;
import com.gongzone.central.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    /*@PostMapping("/register")
    public ResponseEntity<Member> register(@RequestBody Member member) {
        Member registeredMember = memberService.registerMember(member);
        return ResponseEntity.ok(registeredMember);
    }*/

    @PostMapping("/check")
    public ResponseEntity<Boolean> findMemberById(@RequestBody String memberId) {
        System.out.println("시작 " + memberId);
        Boolean checkId = memberService.getMemberById(memberId);
        System.out.println("checkId = " + checkId);
        return ResponseEntity.ok(checkId);
    }

    @GetMapping("/test")
    public ResponseEntity<Member> getInfo(Authentication authentication) {
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();

        String token = memberDetails.getToken();

        String memberNo = jwtUtil.extractMemberNo(token);

        Member member = memberService.getMemberByNo(memberNo);

        return ResponseEntity.ok(member);
    }


    public ResponseEntity<List<Member>> findAllMembers() {
        return null;
    }

}
