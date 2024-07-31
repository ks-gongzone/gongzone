package com.gongzone.central.member.myInfo.interaction.controller;

import com.gongzone.central.member.login.security.JwtUtil;
import com.gongzone.central.member.login.service.MemberDetails;
import com.gongzone.central.member.myInfo.interaction.domain.InteractionMember;
import com.gongzone.central.member.myInfo.interaction.service.InteractionService;
import com.gongzone.central.member.myInfo.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members/interaction")
public class InteractionController {
    private final InteractionService interactionService;
    private final ProfileService profileService;
    private final JwtUtil jwtUtil;

    // 공통으로 사용할 메서드
    private String extractCurrentUserNo(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String token = ((MemberDetails) authentication.getPrincipal()).getToken();
            return jwtUtil.extractMemberNo(token);
        }
        return null;
    }

    /**
     * @작성일: 2024-07-11
     * @내용: [회원] 유저 조회 및 검색 조회
     */
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAllPublicMembers(
            @RequestParam(required = false, defaultValue = "") String memberName,
            @RequestParam(defaultValue = "") String searchQuery,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size,
            Authentication authentication) {

        String currentUserNo = extractCurrentUserNo(authentication);
        if (currentUserNo == null) {
            return ResponseEntity.status(403).body(Collections.singletonMap("message", "비회원 상호 작용 조회 불가"));
        }

        // 검색어가 없거나 데이터가 없을 때
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            searchQuery = "";
        }

        List<InteractionMember> members;
        int totalMembers;

        // 검색 조건에 해당하는 회원 조회
        if (searchQuery.isEmpty()) {
            members = interactionService.findAllMembers(currentUserNo, memberName, searchQuery, page, size);
            totalMembers = interactionService.getTotalMembers(memberName);
        } else {
            members = interactionService.findAllMembers(currentUserNo, "", searchQuery, page, size);
            totalMembers = interactionService.getTotalMembers(searchQuery);
        }

        Map<String, Object> response = new HashMap<>();
        // 해당 메세지를 받으면 프론트에서 검색 첫페이지로 돌아가게함
        if (members.isEmpty()) {
            response.put("message", "회원정보 없음");
            response.put("memberList", Collections.emptyList());
            response.put("currentPage", 1);
            response.put("totalCount", 0);
            response.put("query", searchQuery);
            return ResponseEntity.ok(response);
        }

        response.put("memberList", members);
        response.put("currentPage", page);
        response.put("totalCount", totalMembers);
        response.put("query", searchQuery);
        return ResponseEntity.ok(response);
    }

    /**
     * @내용: 특정 회원 팔로우 및 차단 목록 조회
     */
    @GetMapping("/{memberNo}/follow")
    public ResponseEntity<Map<String, Object>> getFollowMembers(
            @RequestParam(required = false, defaultValue = "") String memberName,
            @RequestParam(defaultValue = "") String searchQuery,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size,
            Authentication authentication) {

        String currentUserNo = extractCurrentUserNo(authentication);
        if (currentUserNo == null) {
            return ResponseEntity.status(403).body(Collections.singletonMap("message", "비회원 상호 작용 조회 불가"));
        }

        List<InteractionMember> members = interactionService.findAllMembers(currentUserNo, memberName, searchQuery, page, size);

        List<InteractionMember> followMembers = members.stream()
                .filter(InteractionMember::isFollowing)
                .collect(Collectors.toList());

        int totalMembers = followMembers.size();

        if (followMembers.isEmpty()) {
            return ResponseEntity.status(404).body(Collections.singletonMap("message", "조회된 회원 정보 없음")); // 조회된 회원 정보가 없는 경우 404 반환
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
            @RequestParam(defaultValue = "") String searchQuery,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size,
            Authentication authentication) {

        String currentUserNo = extractCurrentUserNo(authentication);
        if (currentUserNo == null) {
            return ResponseEntity.status(403).body(Collections.singletonMap("message", "비회원 상호 작용 조회 불가"));
        }

        List<InteractionMember> members = interactionService.findAllMembers(currentUserNo, memberName, searchQuery, page, size);

        List<InteractionMember> blockMembers = members.stream()
                .filter(InteractionMember::isBlocked)
                .collect(Collectors.toList());

        int totalMembers = blockMembers.size();

        if (blockMembers.isEmpty()) {
            return ResponseEntity.status(404).body(Collections.singletonMap("message", "조회된 회원 정보 없음")); // 조회된 회원 정보가 없는 경우 404 반환
        }

        Map<String, Object> response = new HashMap<>();
        response.put("blockList", blockMembers);
        response.put("currentPage", page);
        response.put("totalCount", totalMembers);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/follow")
    public ResponseEntity<Void> followMember(
            @RequestBody Map<String, String> request,
            Authentication authentication) {

        String currentUserNo = extractCurrentUserNo(authentication);
        if (currentUserNo == null) {
            return ResponseEntity.status(403).build();
        }

        String targetMemberNo = request.get("targetMemberNo");

        interactionService.followMember(currentUserNo, targetMemberNo);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/follow")
    public ResponseEntity<Void> unfollowMember(
            @RequestBody Map<String, String> request,
            Authentication authentication) {

        String currentUserNo = extractCurrentUserNo(authentication);
        if (currentUserNo == null) {
            return ResponseEntity.status(403).build();
        }

        String targetMemberNo = request.get("targetMemberNo");

        interactionService.unFollowMember(currentUserNo, targetMemberNo);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/block")
    public ResponseEntity<Void> blockMember(
            @RequestBody Map<String, String> request,
            Authentication authentication) {

        String currentUserNo = extractCurrentUserNo(authentication);
        if (currentUserNo == null) {
            return ResponseEntity.status(403).build();
        }

        String targetMemberNo = request.get("targetMemberNo");

        interactionService.blockMember(currentUserNo, targetMemberNo);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/block")
    public ResponseEntity<Void> unBlockMember(
            @RequestBody Map<String, String> request,
            Authentication authentication) {

        String currentUserNo = extractCurrentUserNo(authentication);
        if (currentUserNo == null) {
            return ResponseEntity.status(403).build();
        }

        String targetMemberNo = request.get("targetMemberNo");

        interactionService.unBlockMember(currentUserNo, targetMemberNo);
        return ResponseEntity.ok().build();
    }
}
