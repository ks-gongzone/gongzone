package com.gongzone.central.member.socialLogin.kakao.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongzone.central.member.socialLogin.google.domain.GoogleRequest;
import com.gongzone.central.member.socialLogin.kakao.domain.KakaoRequest;
import com.gongzone.central.member.socialLogin.kakao.service.KakaoService;
import com.gongzone.central.member.socialLogin.naver.domain.SocialMember;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/kakao")
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;
    private final ObjectMapper objectMapper;

    @PostMapping("/token")
    public ResponseEntity<Map<String, Object>> kakaoToken(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            String requestBody = (String) request.getAttribute("requestBody");
            if (requestBody == null || requestBody.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            String userAgent = request.getHeader("User-Agent");
            KakaoRequest kakaoRequest = objectMapper.readValue(requestBody, KakaoRequest.class);
            kakaoRequest.setUserAgent(userAgent);

            if (kakaoRequest.getCode() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Map<String, Object> kakaoResponse = kakaoService.kakaoToken(kakaoRequest.getCode(), kakaoRequest.getUserAgent());
            SocialMember socialMember = (SocialMember) kakaoResponse.get("socialMember");
            if ((Boolean) kakaoResponse.get("isNewMember")) {
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
