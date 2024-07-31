package com.gongzone.central.member.myInfo.controller;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.login.security.JwtUtil;
import com.gongzone.central.member.login.service.MemberDetails;
import com.gongzone.central.member.myInfo.domain.MyInformation;
import com.gongzone.central.member.myInfo.service.MyInfoService;
import com.gongzone.central.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
public class MyInfoController {
    private final MyInfoService myInfoService;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @Autowired
    public MyInfoController(MyInfoService myInfoService, MemberService memberService, JwtUtil jwtUtil) {
        this.myInfoService = myInfoService;
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * @제목 : 사용자 세션 값 관리
     * @작성일: 2024-06-19
     * @수정일: 2024-06-19
     * @내용: 세션의 존재에 따라 1 또는 0반환 후 로직 처리 기대
     */
    /*
    @GetMapping("/{memberNo}/session")
    public String getSessionMember(@PathVariable String memberNo) {
        Member member = myInfoService.findByNo(memberNo);
        return (member != null) ? "1" : "0";
    }
     */

    /**
     * @제목: 특정회원 정보 토큰
     * @작성일: 2024-06-19
     * @수정일: 2024-06-19
     * @내용: 로그인 된 특정회원 정보 return
     */
    @GetMapping("/{memberNo}/memberInfo")
    public ResponseEntity<Member> getMemberInfo(@PathVariable String memberNo, Authentication authentication) {
        String token = ((MemberDetails) authentication.getPrincipal()).getToken();
        String extractedMemberNo = jwtUtil.extractMemberNo(token);

        if (!extractedMemberNo.equals(memberNo)) {
            return ResponseEntity.status(403).build();
        }

        Member member = myInfoService.findByNo(memberNo);
        if (member != null) {
            return ResponseEntity.ok(member);
        } else {
            throw new RuntimeException("회원 정보를 가져올 수 없습니다.");
        }
    }

    // 2024-06-26 JWT토큰 유효성 검증 로직 추가
    @PostMapping("/{memberNo}/password")
    public ResponseEntity<String> updatePassword(@PathVariable String memberNo, @RequestBody MyInformation myInformation, Authentication authentication) {
        String token = ((MemberDetails) authentication.getPrincipal()).getToken();
        String extractedMemberNo = jwtUtil.extractMemberNo(token);

        if (!extractedMemberNo.equals(memberNo)) {
            return ResponseEntity.status(403).body("유효 토큰이 아닙니다.");
        }

        Member member = myInfoService.findByNo(memberNo);
        if (member == null) {
            return ResponseEntity.status(404).body("로그인된 사용자가 없습니다.");
        }
        try {
            myInfoService.updatePassword(member, myInformation);
            return ResponseEntity.ok("비밀번호 변경 성공.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/{memberNo}/nickname")
    public ResponseEntity<Map<String, String>> getNickname(@PathVariable String memberNo, Authentication authentication) {
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        String token = memberDetails.getToken();
        String extractedMemberNo = jwtUtil.extractMemberNo(token);

        if (!extractedMemberNo.equals(memberNo)) {
            return ResponseEntity.status(403).build();
        }
        Member member = myInfoService.findByNo(memberNo);
        if (member != null) {
            Map<String, String> response = new HashMap<>();
            response.put("nickname", member.getMemberNick());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/{memberNo}/nickname")
    public ResponseEntity<Void> updateNickname(@PathVariable String memberNo, @RequestBody MyInformation myInformation, Authentication authentication) {

        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        String token = memberDetails.getToken();
        String extractedMemberNo = jwtUtil.extractMemberNo(token);

        if (!extractedMemberNo.equals(memberNo)) {
            return ResponseEntity.status(403).build();
        }

        Member member = myInfoService.findByNo(memberNo);

        myInfoService.updateMemberNick(member, myInformation);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{memberNo}/locations")
    public ResponseEntity<Map<String, String>> getAddress(@PathVariable String memberNo, Authentication authentication) {
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        String token = memberDetails.getToken();
        String extractedMemberNo = jwtUtil.extractMemberNo(token);

        if (!extractedMemberNo.equals(memberNo)) {
            return ResponseEntity.status(403).build();
        }

        Member member = myInfoService.findByNo(memberNo);
        if (member != null) {
            Map<String, String> response = new HashMap<>();
            String address = member.getMemberAddress();
            if (address == null || address.trim().isEmpty()) {
                response.put("memberAddress", "");
            } else {
                response.put("memberAddress", address);
            }

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/{memberNo}/locations")
    public ResponseEntity<Void> updateMemberAddress(@PathVariable String memberNo, @RequestBody MyInformation myInformation, Authentication authentication) {
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        String token = memberDetails.getToken();
        String extractedMemberNo = jwtUtil.extractMemberNo(token);

        if (!extractedMemberNo.equals(memberNo)) {
            return ResponseEntity.status(403).build();
        }
        Member member = myInfoService.findByNo(memberNo);
        if (member == null) {
            return ResponseEntity.status(404).build();
        }
        myInfoService.updateMemberAddress(member, myInformation);
        return ResponseEntity.ok().build();
    }

    /**
     * @제목: 핸드폰 번호 조회
     * @작성일: 2024-06-28
     * @수정일: 2024-06-28
     */
    @GetMapping("/{memberNo}/phone")
    public ResponseEntity<Map<String, String>> getMemberPhone(@PathVariable String memberNo, Authentication authentication) {
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        String token = memberDetails.getToken();
        String extractedMemberNo = jwtUtil.extractMemberNo(token);

        if (!extractedMemberNo.equals(memberNo)) {
            return ResponseEntity.status(403).build();
        }
        Member member = myInfoService.findByPhone(memberNo);
        if (member != null) {
            Map<String, String> response = new HashMap<>();
            response.put("phone", member.getMemberPhone());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/{memberNo}/updateStatus")
    public ResponseEntity<String> updateStatus(@PathVariable String memberNo, @RequestBody MyInformation myInformation, Authentication authentication) {
        String token = ((MemberDetails) authentication.getPrincipal()).getToken();
        String extractedMemberNo = jwtUtil.extractMemberNo(token);

        if (!extractedMemberNo.equals(memberNo)) {
            return ResponseEntity.status(403).body("유효 토큰이 아닙니다.");
        }

        myInfoService.updateStatusCode(memberNo, myInformation.getNewStatusCode());
        return ResponseEntity.ok("상태 코드 변경 성공.");
    }
}