package com.gongzone.central.member.socialLogin.naver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongzone.central.member.login.domain.LoginLog;
import com.gongzone.central.member.login.service.LoginLogService;
import com.gongzone.central.member.socialLogin.naver.domain.NaverRequest;
import com.gongzone.central.member.socialLogin.naver.domain.SocialMember;
import com.gongzone.central.member.socialLogin.naver.service.NaverService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/naver")
@RequiredArgsConstructor
public class NaverController {

    private final NaverService naverService;
    private final ObjectMapper objectMapper;

    @PostMapping("/token")
    public ResponseEntity<SocialMember> naverToken(HttpServletRequest request) {
        try {
            String requestBody = (String) request.getAttribute("requestBody");
            if (requestBody == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            String userAgent = request.getHeader("User-Agent");
            NaverRequest naverRequest = objectMapper.readValue(requestBody, NaverRequest.class);
            naverRequest.setUserAgent(userAgent);

            if (naverRequest.getCode() == null || naverRequest.getState() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            SocialMember socialMember = naverService.naverToken(naverRequest.getCode(), naverRequest.getUserAgent());

            return new ResponseEntity<>(socialMember, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
