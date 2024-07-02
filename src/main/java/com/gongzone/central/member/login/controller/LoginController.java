package com.gongzone.central.member.login.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.login.domain.LoginRequest;
import com.gongzone.central.member.login.domain.LoginResponse;
import com.gongzone.central.member.login.mapper.LoginMapper;
import com.gongzone.central.member.login.security.JwtUtil;
import com.gongzone.central.member.login.service.CheckStatusCode;
import com.gongzone.central.member.login.service.MemberDetails;
import com.gongzone.central.member.login.service.MemberDetailsService;
import com.gongzone.central.point.domain.Point;
import com.gongzone.central.point.mapper.PointMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class LoginController {

    private final JwtUtil jwtUtil;
    private final MemberDetailsService memberDetailsService;
    private final AuthenticationManager authenticationManager;
    private final CheckStatusCode checkStatusCode;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) throws AuthenticationException {
        try {
            // 사용자 인증
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getLoginId(), loginRequest.getLoginPw())
            );

            // 사용자 정보 로드
            final MemberDetails memberDetails = (MemberDetails) memberDetailsService.loadUserByUsername(loginRequest.getLoginId());
            // JWT 토큰 생성

            final String jwt = jwtUtil.generateToken(memberDetails);
            final long expiresIn = jwtUtil.extractExpiration(jwt).getTime();
            final String refreshToken = jwtUtil.generateRefreshToken(memberDetails);

            /*if (jwt.chars().filter(ch -> ch == '.').count() != 2) {
                System.out.println("JWT 문자열이 올바르게 형식화되지 않았습니다. 정확히 2개의 마침표가 포함되어야 합니다. 발견된 마침표 수: " + jwt.chars().filter(ch -> ch == '.').count());
            } else {
                // JWT가 올바르게 형식화된 경우 checkStatus 메서드 실행
                checkStatusCode.checkStatus(memberDetails.getMemberNo(), response);
                System.out.println("checkStatusCode 실행");
            }*/

            System.out.println("1123123123123123");
            System.out.println("memberNo  : " + jwtUtil.extractMemberNo(memberDetails.getMemberNo()));
            checkStatusCode.checkStatus(jwtUtil.extractMemberNo(memberDetails.getMemberNo()), response);
            System.out.println("checkStatusCode 실행");

            // 토큰을 포함한 응답 반환
            return ResponseEntity.ok(new LoginResponse("bearer", jwt, expiresIn, refreshToken, memberDetails.getMemberNo(), memberDetails.getPointNo(),null));

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("존재하지 않는 사용자 입니다."));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("잘못된 사용자이거나 비밀번호가 일치하지 않습니다."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginResponse("로그인 중 오류가 발생했습니다."));
        }
    }

}

