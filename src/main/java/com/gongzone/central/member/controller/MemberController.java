package com.gongzone.central.member.controller;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.login.security.JwtUtil;
import com.gongzone.central.member.login.service.MemberDetails;
import com.gongzone.central.member.service.MemberService;
import com.gongzone.central.point.domain.Point;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Member member) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean registeredMember  = memberService.registerMember(member);
            if (registeredMember ) {
                result.put("success", true);
            } else {
                result.put("success", false);
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @PostMapping("/check")
    public ResponseEntity<Boolean> findMemberById(@RequestBody Map<String, String> request) {
        String memberId = request.get("memberId");
        System.out.println("시작 " + memberId);
        Boolean checkId = memberService.getMemberById(memberId);
        System.out.println("checkId = " + checkId);
        if(!checkId) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
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
