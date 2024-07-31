package com.gongzone.central.member.myInfo.profile.controller;

import com.gongzone.central.file.domain.FileUpload;
import com.gongzone.central.member.login.security.JwtUtil;
import com.gongzone.central.member.login.service.MemberDetails;
import com.gongzone.central.member.myInfo.profile.domain.Profile;
import com.gongzone.central.member.myInfo.profile.service.ProfileService;
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
            return ResponseEntity.status(201).body("프로필 사진 저장 성공");
        } catch (Exception e) {
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
            return ResponseEntity.status(200).body(fileUpload);
        } catch (Exception e) {
            e.printStackTrace();
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
            Profile profile = profileService.getProfile(memberNo);
            if (profile != null) {

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
            return ResponseEntity.status(500).body(null);
        }
    }

    // 전체 회원 프로필 사진 조회
    @GetMapping("/allProfiles")
    public ResponseEntity<List<Profile>> getAllProfiles() {
        try {
            List<Profile> profiles = profileService.getAllProfiles();
            if (profiles != null) {
                return ResponseEntity.ok(profiles);
            } else {
                return ResponseEntity.status(404).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // 파일 파싱 API
    @PostMapping("/parseFile")
    public ResponseEntity<FileUpload> parseFile(Authentication authentication, @RequestParam("file") MultipartFile file) {
        String token = ((MemberDetails) authentication.getPrincipal()).getToken();
        String memberNo = jwtUtil.extractMemberNo(token);

        if (memberNo == null) {
            return ResponseEntity.status(401).body(null);
        }

        try {
            FileUpload fileUpload = profileService.parseFile(file);
            return ResponseEntity.status(200).body(fileUpload);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}