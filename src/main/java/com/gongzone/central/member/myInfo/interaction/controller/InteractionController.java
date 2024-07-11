package com.gongzone.central.member.myInfo.interaction.controller;

import com.gongzone.central.member.login.security.JwtUtil;
import com.gongzone.central.member.login.service.MemberDetails;
import com.gongzone.central.member.myInfo.interaction.domain.InteractionMember;
import com.gongzone.central.member.myInfo.interaction.service.InteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
public class InteractionController {
    private final InteractionService interactionService;
    private final JwtUtil jwtUtil;

    /**
     * @작성일: 2024-07-11
     * @내용: [회원] 유저 조회
     */
    @GetMapping("/interaction/{memberNo}")
    public ResponseEntity<InteractionMember> getMemberInfo(
            @PathVariable String memberNo,
            Authentication authentication) {
        String token = ((MemberDetails) authentication.getPrincipal()).getMemberNo();
        String currentUserNo = jwtUtil.extractMemberNo(token);

        System.out.println("[컨트롤러] 회원 상호 작용 조회" + currentUserNo);
        if (currentUserNo == null) {
            System.out.println("[컨트롤러] 회원 상호 작용 조회, 로그인이 필요합니다.");
            return ResponseEntity.status(403).body(null);
        }
        if (memberNo == null || memberNo.trim().isEmpty()) {
            System.out.println("[컨트롤러] 회원 상호 작용 조회, 요청 회원 번호확인 바람");
            return ResponseEntity.status(400).body(null);
        }

        InteractionMember member = interactionService.getMemberByNo(memberNo, currentUserNo);
        if (member == null) {
            System.out.println("[컨트롤러] 회원 상호 작용 조회, 해당 회원 정보 없음");
        }
        return ResponseEntity.ok(member);
    }
    /**
     * @작성일: 2024-07-11
     * @내용: [비회원] 유저 조회 및 검색 조회
     */
    @GetMapping("/interaction")
    public ResponseEntity<Map<String, Object>> getAllPublicMembers(
            @RequestParam(required = false, defaultValue = "") String memberName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "15") int size,
            Authentication authentication) {
        String currentUserNo = null;

        if (authentication != null && authentication.isAuthenticated()) {
            String token = ((MemberDetails) authentication.getPrincipal()).getToken();
            currentUserNo = jwtUtil.extractMemberNo(token);
            System.out.println("[컨트롤러] 로그인된 사용자: " + currentUserNo);
        } else {
            System.out.println("[컨트롤러] 비회원 상호 작용 조회 불가");
            return ResponseEntity.status(403).body(null);
        }

        List<InteractionMember> members = interactionService.findAllMembers(currentUserNo, memberName, page, size);
        if (members.isEmpty()) {
            System.out.println("[컨트롤러] 조회된 회원 정보 없음");
            return ResponseEntity.status(404).body(null); // 조회된 회원 정보가 없는 경우 404 반환
        }

        Map<String, Object> response = new HashMap<>();
        response.put("memberList", members);
        response.put("currentPage", page);
        return ResponseEntity.ok(response);
    }

}
