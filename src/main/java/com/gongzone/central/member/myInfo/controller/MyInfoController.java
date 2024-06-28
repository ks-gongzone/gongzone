package com.gongzone.central.member.myInfo.controller;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.login.security.JwtUtil;
import com.gongzone.central.member.login.service.MemberDetails;
import com.gongzone.central.member.myInfo.domain.MyInformation;
import com.gongzone.central.member.myInfo.service.MyInfoService;
import com.gongzone.central.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

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

        if(!extractedMemberNo.equals(memberNo)) {
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
        System.out.println("getNickname 호출");
        System.out.println("memberNo: " + memberNo);

        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        String token = memberDetails.getToken();
        String extractedMemberNo = jwtUtil.extractMemberNo(token);

        System.out.println("토큰에 담긴 멤버 정보: " + extractedMemberNo);

        if (!extractedMemberNo.equals(memberNo)) {
            System.out.println("인증 실패");
            return ResponseEntity.status(403).build();
        }
        Member member = myInfoService.findByNo(memberNo);
        if(member != null) {
            Map<String, String> response = new HashMap<>();
            response.put("nickname", member.getMemberNick());
            System.out.println("닉네임: " + member.getMemberNick()); // 테스트 후 삭제
            return ResponseEntity.ok(response);
        } else {
            System.out.println("회원 정보 없음");
            return ResponseEntity.status(404).build(); // 정보 없음 404에러
        }
    }

    @PostMapping("/{memberNo}/nickname")
    public ResponseEntity<Void> updateNickname(@PathVariable String memberNo, @RequestBody MyInformation myInformation, Authentication authentication) {
        System.out.println("updateNickname 메서드 호출");
        System.out.println("memberNo: " + memberNo);
        System.out.println("newMemberNick: " + myInformation.getNewMemberNick());

        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        String token = memberDetails.getToken();
        String extractedMemberNo = jwtUtil.extractMemberNo(token);

        System.out.println("토큰정보: " + extractedMemberNo);

        if (!extractedMemberNo.equals(memberNo)) {
            System.out.println("인증 실패");
            return ResponseEntity.status(403).build();
        }

        Member member = myInfoService.findByNo(memberNo);
        if (member != null) {
            System.out.println("회원 정보 존재");
        } else {
            System.out.println("회원 정보 없음");
        }

        myInfoService.updateMemberNick(member, myInformation);
        System.out.println("닉네임 수정 완료");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{memberNo}/locations")
    public ResponseEntity<Map<String, String>> getAddress(@PathVariable String memberNo, Authentication authentication) {
        System.out.println("장소 메서드 호출 (조회)");
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        String token = memberDetails.getToken();
        String extractedMemberNo = jwtUtil.extractMemberNo(token);

        if (!extractedMemberNo.equals(memberNo)) {
            System.out.println("장소 조회 중 인증 실패");
            return ResponseEntity.status(403).build();
        }

        Member member = myInfoService.findByNo(memberNo);
        if (member != null) {
            Map<String, String> response = new HashMap<>();
            String address = member.getMemberAddress();
            if(address == null || address.trim().isEmpty()) {
                System.out.println("저장된 주소가 없습니다.");
                response.put("memberAddress", "");
            } else {
                response.put("memberAddress", address);
            }

            System.out.println("주소: " + address);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/{memberNo}/locations")
    public ResponseEntity<Void> updateMemberAddress(@PathVariable String memberNo, @RequestBody MyInformation myInformation, Authentication authentication) {
        System.out.println("장소 메서드 호출(수정)");
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        String token = memberDetails.getToken();
        String extractedMemberNo = jwtUtil.extractMemberNo(token);

        if (!extractedMemberNo.equals(memberNo)) {
            System.out.println("장소수정중 403에러 처리");
            return ResponseEntity.status(403).build();
        }
        Member member = myInfoService.findByNo(memberNo);
        if (member == null) {
            System.out.println("장소수정중 404에러 처리");
            return ResponseEntity.status(404).build();
        }
        myInfoService.updateMemberAddress(member,myInformation);
        return ResponseEntity.ok().build();
    }

    /**
     * @제목: 핸드폰 번호 조회
     * @작성일: 2024-06-28
     * @수정일: 2024-06-28
     */
    @GetMapping("/{memberNo}/phone")
    public ResponseEntity<Map<String, String>> getMemberPhone(@PathVariable String memberNo, Authentication authentication) {
        System.out.println("핸드폰번호 조회");
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        String token = memberDetails.getToken();
        String extractedMemberNo = jwtUtil.extractMemberNo(token);

        if (!extractedMemberNo.equals(memberNo)) {
            System.out.println("핸드폰 번호 조회 실패");
            return ResponseEntity.status(403).build();
        }
        Member member = myInfoService.findByPhone(memberNo);
        if (member != null) {
            Map<String,String> response = new HashMap<>();
            response.put("phone", member.getMemberPhone());
            return ResponseEntity.ok(response);
        } else {
            System.out.println("해당 회원이 없습니다.");
            return ResponseEntity.status(404).build();
        }
    }
}


