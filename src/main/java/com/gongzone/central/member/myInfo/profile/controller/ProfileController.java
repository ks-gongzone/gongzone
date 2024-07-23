package com.gongzone.central.member.myInfo.profile.controller;

import com.gongzone.central.member.login.security.JwtUtil;
import com.gongzone.central.member.login.service.MemberDetails;
import com.gongzone.central.member.myInfo.profile.domain.Profile;
import com.gongzone.central.member.myInfo.profile.service.ProfileService;
import com.gongzone.central.file.domain.FileUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    private final JwtUtil jwtUtil;

    // 프로필 생성
    @PostMapping("/addProfilePicture")
    public ResponseEntity<String> addProfilePicture(Authentication authentication, @RequestParam("file") MultipartFile file) {
        String token = ((MemberDetails) authentication.getPrincipal()).getToken();
        String memberNo = jwtUtil.extractMemberNo(token);

        if (memberNo == null) {
            return ResponseEntity.status(401).body("유효 토큰이 아닙니다.");
        }

        try {
            profileService.addProfilePicture(memberNo, file);
            System.out.println("[컨트롤러] 사진 추가" + file);
            return ResponseEntity.status(201).body("프로필 사진 저장 성공");
        } catch (Exception e) {
            System.out.println("프로필 사진 저장 실패: " + e.getMessage());
            return ResponseEntity.status(500).body("내부 서버 오류.");
        }
    }

    // 프로필 수정
    @PostMapping("/updateProfilePicture")
    public ResponseEntity<FileUpload> updateProfilePicture(Authentication authentication, @RequestParam("file") MultipartFile file) {
        String token = ((MemberDetails) authentication.getPrincipal()).getToken();
        String memberNo = jwtUtil.extractMemberNo(token);

        if (memberNo == null) {
            return ResponseEntity.status(401).body(null);
        }

        try {
            FileUpload fileUpload = profileService.updateProfilePicture(memberNo, file);
            System.out.println("[컨트롤러] 사진 추가: " + file.getOriginalFilename());
            return ResponseEntity.status(200).body(fileUpload);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("프로필 사진 저장 실패: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    // 프로필 조회
    @GetMapping("/getProfile/{memberNo}")
    public ResponseEntity<Map<String, Object>> getProfile(Authentication authentication, @PathVariable String memberNo) {
        String token = ((MemberDetails) authentication.getPrincipal()).getToken();
        String authenticatedMemberNo = jwtUtil.extractMemberNo(token);

        if (authenticatedMemberNo == null || !authenticatedMemberNo.equals(memberNo)) {
            return ResponseEntity.status(401).body(null);
        }

        try {
            System.out.println("전달된 memberNo: " + memberNo);
            Profile profile = profileService.getProfile(memberNo);
            if (profile != null) {
                System.out.println("[컨트롤러 시작]");
                System.out.println("파일: " + profile.getFiles());
                System.out.println("작성 글 수" + profile.getBoardCount());
                System.out.println("팔로워 수" + profile.getFollower());
                System.out.println("팔로잉 수" + profile.getFollowing());

                // 파일 데이터가 있으면 첫 번째 파일을 file 필드로 추가
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("profile", profile);
                if (profile.getFiles() != null && !profile.getFiles().isEmpty()) {
                    responseMap.put("file", profile.getFiles().get(0));
                }
                return ResponseEntity.status(200).body(responseMap);
            } else {
                return ResponseEntity.status(404).body(null);
            }
        } catch (Exception e) {
            System.out.println("[컨트롤러] 프로필 조회 실패: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    // 전체 회원 프로필 사진 조회
    @GetMapping("/allProfiles")
    public ResponseEntity<List<Profile>> getAllProfiles() {
        try {
            List<Profile> profiles = profileService.getAllProfiles();
            if (profiles != null) {
                System.out.println("[컨트롤러] 전체 회원 프로필" + profiles);
                return ResponseEntity.ok(profiles);
            } else {
                return ResponseEntity.status(404).body(null);
            }
        } catch (Exception e) {
            System.out.println("[컨트롤러] 전체 프로필 조회 실패: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }
}