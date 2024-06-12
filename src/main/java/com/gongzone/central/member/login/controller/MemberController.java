package com.gongzone.central.member.login.controller;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 전체 사용자 목록 조회
    @GetMapping
    public ResponseEntity<List<Member>> getAllMember() {
        List<Member> member = MemberService.findAll();
        return ResponseEntity.ok(member);
    }

    // 특정 사용자 조회 (number로 조회)
    @GetMapping("/{memberNo}")
    public ResponseEntity<Member> getMemberByNumber(@PathVariable String memberNo) {
        Member member = MemberService.findByNumber(memberNo);
        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(member);
    }

    // 소셜 로그인 사용자 조회

    // 새 사용자 생성 (일반 사용자)
    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody Member member) {
        MemberService.insert(member);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 사용자 로그인 (일반 로그인)
    @PostMapping("/login")
    public ResponseEntity<Member> loginMember(@RequestBody LoginRequest loginRequest) {
        Member member = MemberService.login(loginRequest.getMembername(), loginRequest.getPassword());
        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(member);
    }

    /*@GetMapping("/social")
    public ResponseEntity<Member> getUserBySocialId(@RequestParam String provider, @RequestParam String socialId) {
        Member member = MemberService.findBySocialId(provider, socialId);
        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(member);
    }*/

    // 소셜 로그인 사용자 생성
    /*@PostMapping("/social-login")
    public ResponseEntity<Member> socialLogin(@RequestBody SocialLoginRequest socialLoginRequest) {
        Member member = MemberService.socialLogin(socialLoginRequest.getProvider(), socialLoginRequest.getSocialId());
        return ResponseEntity.ok(member);
    }*/

}
