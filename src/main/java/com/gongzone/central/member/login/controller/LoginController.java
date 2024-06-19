package com.gongzone.central.member.login.controller;

import com.gongzone.central.member.login.domain.LoginRequest;
import com.gongzone.central.member.login.domain.LoginResponse;
import com.gongzone.central.member.login.security.JwtUtil;
import com.gongzone.central.member.login.service.MemberDetails;
import com.gongzone.central.member.login.service.MemberDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3001")
public class LoginController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MemberDetailsService memberDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws AuthenticationException {
        try {
            System.out.println("Attempting to load user: " + loginRequest.getLoginId());
            // 사용자 정보 로드
            final MemberDetails memberDetails = (MemberDetails) memberDetailsService.loadUserByUsername(loginRequest.getLoginId());

            // 사용자 인증
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getLoginId(), loginRequest.getLoginPw())
            );

            // JWT 토큰 생성
            final String jwt = jwtUtil.generateToken(memberDetails);
            final long expiresIn = jwtUtil.getExpirationDateFromToken(jwt).getTime();

            // 토큰을 포함한 응답 반환
            return ResponseEntity.ok(new LoginResponse("bearer", jwt, expiresIn, null));

        } catch (UsernameNotFoundException e) {
            System.out.println("UsernameNotFoundException: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("존재하지 않는 사용자 입니다."));
        } catch (BadCredentialsException e) {
            System.out.println("AuthenticationException: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("잘못된 사용자이거나 비밀번호가 일치하지 않습니다."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginResponse("로그인 중 오류가 발생했습니다."));
        }
    }
}

