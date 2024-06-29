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

@RestController
@RequestMapping("/api/google")
@RequiredArgsConstructor
public class GoogleController {

    private final GoogleService googleService;
    private final ObjectMapper objectMapper;

    @PostMapping("/token")
    public ResponseEntity<SocialMember> googleToken(HttpServletRequest request) {
        try {
            System.out.println("1");
            String requestBody = (String) request.getAttribute("requestBody");
            if (requestBody == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            GoogleRequest googleRequest = objectMapper.readValue(requestBody, GoogleRequest.class);

            System.out.println("Received NaverRequest: " + googleRequest);
            System.out.println("code = " + googleRequest.getCode());
            System.out.println("state = " + googleRequest.getState());

            if (googleRequest.getCode() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            SocialMember socialMember = googleService.googleToken(googleRequest.getCode());
            return new ResponseEntity<>(socialMember, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
