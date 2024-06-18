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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class LoginController {

    //private final LoginService loginService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MemberDetailsService memberDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

 /*   public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }*/

    /*@PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        try {
            Member member = loginService.login(loginRequest.getLoginId(), loginRequest.getLoginPw());
            session.setAttribute("member", member);
            return ResponseEntity.ok().body(new LoginResponse(loginRequest.getLoginId()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(e.getMessage()));
        }
    }*/

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws AuthenticationException {
        try {
            // 사용자 정보 로드
            final MemberDetails memberDetails = (MemberDetails) memberDetailsService.loadUserByUsername(loginRequest.getLoginId());

            // 비밀번호 검증 (해시를 사용하지 않고 평문 비교)
            if (!loginRequest.getLoginPw().equals(memberDetails.getPassword())) {
                throw new AuthenticationException("Incorrect username or password") {};
            }

            // 사용자 인증
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getLoginId(), loginRequest.getLoginPw())
            );

            // JWT 토큰 생성
            final String jwt = jwtUtil.generateToken(memberDetails);

            // 토큰을 포함한 응답 반환
            return ResponseEntity.ok(new LoginResponse(jwt));
        } catch (AuthenticationException e) {
            // 인증 실패 시 적절한 응답 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(null, "Incorrect username or password"));
        }
    }
}

