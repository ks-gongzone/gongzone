package com.gongzone.central.member.myInfo.interaction.controller;

import com.gongzone.central.member.login.security.JwtUtil;
import com.gongzone.central.member.login.service.MemberDetails;
import com.gongzone.central.member.myInfo.interaction.domain.InteractionMember;
import com.gongzone.central.member.myInfo.interaction.service.InteractionService;
import com.gongzone.central.member.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members/interaction")
public class InteractionController {
    private final InteractionService interactionService;
    private final JwtUtil jwtUtil;

    /**
     * @작성일: 2024-07-11
     * @내용: [회원] 유저 조회 및 검색 조회
     */
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAllPublicMembers(
            @RequestParam(required = false, defaultValue = "") String memberName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size,
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
        int totalMembers = interactionService.getTotalMembers();
        if (members.isEmpty()) {
            System.out.println("[컨트롤러] 조회된 회원 정보 없음");
            return ResponseEntity.status(404).body(null); // 조회된 회원 정보가 없는 경우 404 반환
        }

        Map<String, Object> response = new HashMap<>();
        response.put("memberList", members);
        response.put("currentPage", page);
        response.put("totalCount", totalMembers);
        return ResponseEntity.ok(response);
    }

    /**
     * @내용: 특정 회원 팔로우 및 차단 목록 조회
     */
    @GetMapping("/{memberNo}/follow")
    public ResponseEntity<Map<String, Object>> getFollowMembers(
            @RequestParam(required = false, defaultValue = "") String memberName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size,
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

        List<InteractionMember> followMembers = members.stream()
                .filter(InteractionMember::isFollowing)
                .collect(Collectors.toList());

        int totalMembers = followMembers.size();

        if (followMembers.isEmpty()) {
            System.out.println("[컨트롤러] 조회된 회원 정보 없음");
            return ResponseEntity.status(404).body(null); // 조회된 회원 정보가 없는 경우 404 반환
        }

        Map<String, Object> response = new HashMap<>();
        response.put("followList", followMembers);
        response.put("currentPage", page);
        response.put("totalCount", totalMembers);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{memberNo}/block")
    public ResponseEntity<Map<String, Object>> getBlockMembers(
            @RequestParam(required = false, defaultValue = "") String memberName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size,
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

        List<InteractionMember> blockMembers = members.stream()
                .filter(InteractionMember::isBlocked)
                .collect(Collectors.toList());

        int totalMembers = blockMembers.size();

        if (blockMembers.isEmpty()) {
            System.out.println("[컨트롤러] 조회된 회원 정보 없음");
            return ResponseEntity.status(404).body(null); // 조회된 회원 정보가 없는 경우 404 반환
        }

        Map<String, Object> response = new HashMap<>();
        response.put("blockList", blockMembers);
        response.put("currentPage", page);
        response.put("totalCount", totalMembers);
        return ResponseEntity.ok(response);
    }
    /**
     * @작성일: 2024-07-12
     * @수정일: 2024-07-12
     * @내용: 팔로잉 컨트롤러
     */
    @PostMapping("/follow")
    public ResponseEntity<Void> followMember(
            @RequestBody Map<String, String> request,
            Authentication authentication) {
        String token = ((MemberDetails) authentication.getPrincipal()).getToken();
        String currentUserNo = jwtUtil.extractMemberNo(token);
        String targetMemberNo = request.get("targetMemberNo");

        System.out.println("[컨트롤러] 팔로잉 유저" + currentUserNo);
        System.out.println("[컨트롤러] 팔로잉 타겟" + targetMemberNo);

        if (currentUserNo == null) {
            System.out.println("[컨틀롤러] 팔로잉: 로그인 오류");
            return ResponseEntity.status(403).build();
        }
        // currentUser와 targetMember가 같이 있는 행이 존재하는지 판별 후 존재 시 에러로직 추가
        interactionService.followMember(currentUserNo, targetMemberNo);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/follow")
    public ResponseEntity<Void> unfollowMember(
            @RequestBody Map<String, String> request,
            Authentication authentication) {

        String token = ((MemberDetails) authentication.getPrincipal()).getToken();
        String currentUserNo = jwtUtil.extractMemberNo(token);
        String targetMemberNo = request.get("targetMemberNo");

        System.out.println("[컨트롤러] 언팔로우 유저" + currentUserNo);
        System.out.println("[컨트롤러] 언팔로우 타겟" + targetMemberNo);

        if (currentUserNo == null) {
            return ResponseEntity.status(403).build();
        }
        interactionService.unFollowMember(currentUserNo, targetMemberNo);
        return ResponseEntity.ok().build();
    }
    /**
     * @작성일: 2024-07-12
     * @수정일: 2024-07-12
     * @내용: 차단 컨트롤러
     */
    @PostMapping("/block")
    public ResponseEntity<Void> blockMember(
            @RequestBody Map<String, String> request,
            Authentication authentication) {
        String token = ((MemberDetails) authentication.getPrincipal()).getToken();
        String currentUserNo = jwtUtil.extractMemberNo(token);
        String targetMemberNo = request.get("targetMemberNo");

        System.out.println("[컨트롤러] 차단 유저" + currentUserNo);
        System.out.println("[컨트롤러] 차단 타겟" + targetMemberNo);

        if (currentUserNo == null) {
            return ResponseEntity.status(403).build();
        }
        interactionService.blockMember(currentUserNo, targetMemberNo);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/block")
    public ResponseEntity<Void> unBlockMember(
            @RequestBody Map<String, String> request,
            Authentication authentication) {
        String token = ((MemberDetails) authentication.getPrincipal()).getToken();
        String currentUserNo = jwtUtil.extractMemberNo(token);
        String targetMemberNo = request.get("targetMemberNo");

        System.out.println("[컨트롤러] 차단 해제 유저" + currentUserNo);
        System.out.println("[컨트롤러] 차단 해제 타겟" + targetMemberNo);

        if (currentUserNo == null) {
            return ResponseEntity.status(403).build();
        }
        interactionService.unBlockMember(currentUserNo, targetMemberNo);
        return ResponseEntity.ok().build();
    }
}
