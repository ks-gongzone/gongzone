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
        System.out.println("1111111111111111122222222222222222");
        Map<String, Object> result = new HashMap<>();
        try {
            boolean registeredMember  = memberService.registerMember(member);
            if (registeredMember ) {
                result.put("success", true);
                System.out.println("result111" + result);
            } else {
                result.put("success", false);
                System.out.println("result222" + result);
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            System.out.println("result333" + result);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @PostMapping("/check/Id")
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

    @PostMapping("/check/Email")
    public ResponseEntity<Boolean> findMemberByEmail(@RequestBody Map<String, String> request) {
        String memberEmail = request.get("memberEmail");
        System.out.println ("시작 " + memberEmail);
        Boolean checkEmail = memberService.getMemberByEmail(memberEmail);
        System.out.println("checkId = " + checkEmail);
        if(!checkEmail) {
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
