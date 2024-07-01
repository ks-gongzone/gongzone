package com.gongzone.central.member.socialLogin.google.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongzone.central.member.socialLogin.google.domain.GoogleRequest;
import com.gongzone.central.member.socialLogin.google.service.GoogleService;
import com.gongzone.central.member.socialLogin.naver.domain.SocialMember;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/google")
@RequiredArgsConstructor
public class GoogleController {

    private final GoogleService googleService;
    private final ObjectMapper objectMapper;

    @PostMapping("/token")
    public ResponseEntity<Map<String, Object>> googleToken(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            String requestBody = (String) request.getAttribute("requestBody");
            if (requestBody == null || requestBody.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            GoogleRequest googleRequest = objectMapper.readValue(requestBody, GoogleRequest.class);

            if (googleRequest.getCode() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Map<String, Object> googleResponse = googleService.googleToken(googleRequest.getCode());
            SocialMember socialMember = (SocialMember) googleResponse.get("socialMember");
            if ((Boolean) googleResponse.get("isNewMember")) {
                result.put("success", true);
                result.put("redirectUrl", "/register?name=" + socialMember.getName() + "&email=" + socialMember.getEmail());
            } else {
                result.put("success", true);
                result.put("token", socialMember.getJwtToken());
                result.put("memberNo", socialMember.getMemberNo());
                result.put("pointNo", socialMember.getPointNo());
                result.put("redirectUrl", "/");
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
}
