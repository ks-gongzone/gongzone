/*
package com.gongzone.central.member.socialLogin.controller;

import com.gongzone.central.member.socialLogin.domain.SocialMember;
import com.gongzone.central.member.socialLogin.service.NaverService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/naver")
public class NaverController {

    private final NaverService naverService;

    // NaverController 생성자
    public NaverController(NaverService naverService) {
        this.naverService = naverService;
    }

    // 네이버 로그인 요청을 처리하는 메소드
    @PostMapping("/token")
    public ResponseEntity<SocialMember> naverToken(@RequestParam String code) {
        try {
            // 네이버 로그인 토큰을 처리하고 사용자 정보를 반환
            SocialMember socialMember = naverService.naverToken(code);
            return new ResponseEntity<>(socialMember, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
*/
