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

    @GetMapping("/test")
    public ResponseEntity<Member> getInfo(Authentication authentication) {
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        System.out.println("memberDetails : " + memberDetails);
        String token = memberDetails.getToken();
        System.out.println("token: " + token);

        String memberNo = jwtUtil.extractMemberNo(token);
        System.out.println("memberNo: " + memberNo);

        Member member = memberService.getMemberByNo(memberNo);
        System.out.println("member : " + member);

        return ResponseEntity.ok(member);
    }


    public ResponseEntity<List<Member>> findAllMembers() {
        return null;
    }

}
