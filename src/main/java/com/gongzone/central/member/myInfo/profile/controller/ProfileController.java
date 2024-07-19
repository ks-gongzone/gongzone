package com.gongzone.central.member.myInfo.profile.controller;

import com.gongzone.central.member.login.security.JwtUtil;
import com.gongzone.central.member.login.service.MemberDetails;
import com.gongzone.central.member.myInfo.profile.domain.Profile;
import com.gongzone.central.member.myInfo.profile.service.ProfileService;
import com.gongzone.central.file.domain.FileUpload;
import com.gongzone.central.file.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    private final JwtUtil jwtUtil;

    @PostMapping("/addProfilePicture")
    public ResponseEntity<String> addProfilePicture(Authentication authentication, @RequestParam("file") MultipartFile file) {
        String token = ((MemberDetails) authentication.getPrincipal()).getToken();
        String memberNo = jwtUtil.extractMemberNo(token);

        if (memberNo == null) {
            return ResponseEntity.status(401).body("유효 토큰이 아닙니다.");
        }

        try {
            profileService.addProfilePicture(memberNo, file);
            return ResponseEntity.status(201).body("Profile picture added successfully!");
        } catch (Exception e) {
            System.out.println("Failed to add profile picture: " + e.getMessage());
            return ResponseEntity.status(500).body("내부 서버 오류.");
        }
    }

    @PostMapping("/updateProfilePicture")
    public ResponseEntity<String> updateProfilePicture(Authentication authentication, @RequestParam("file") MultipartFile file) {
        String token = ((MemberDetails) authentication.getPrincipal()).getToken();
        String memberNo = jwtUtil.extractMemberNo(token);

        if (memberNo == null) {
            return ResponseEntity.status(401).body("유효 토큰이 아닙니다.");
        }

        try {
            profileService.updateProfilePicture(memberNo, file);
            return ResponseEntity.status(200).body("Profile picture updated successfully!");
        } catch (Exception e) {
            System.out.println("Failed to update profile picture: " + e.getMessage());
            return ResponseEntity.status(500).body("내부 서버 오류.");
        }
    }

    @GetMapping("/getProfile/{memberNo}")
    public ResponseEntity<Profile> getProfile(Authentication authentication, @PathVariable String memberNo) {
        String token = ((MemberDetails) authentication.getPrincipal()).getToken();
        String authenticatedMemberNo = jwtUtil.extractMemberNo(token);

        if (authenticatedMemberNo == null || !authenticatedMemberNo.equals(memberNo)) {
            return ResponseEntity.status(401).body(null);
        }

        try {
            Profile profile = profileService.getProfile(memberNo);
            if (profile != null) {
                return ResponseEntity.status(200).body(profile);
            } else {
                return ResponseEntity.status(404).body(null);
            }
        } catch (Exception e) {
            System.out.println("Failed to get profile: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }
}
