package com.gongzone.central.member.login.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.login.domain.*;
import com.gongzone.central.member.login.mapper.LoginMapper;
import com.gongzone.central.member.login.security.JwtUtil;
import com.gongzone.central.member.login.service.CheckStatusCode;
import com.gongzone.central.member.login.service.LoginLogService;
import com.gongzone.central.member.login.service.MemberDetails;
import com.gongzone.central.member.login.service.MemberDetailsService;
import com.gongzone.central.point.domain.Point;
import com.gongzone.central.point.mapper.PointMapper;
import jakarta.servlet.http.HttpServletRequest;
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

import java.util.Date;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class LoginController {

    private final JwtUtil jwtUtil;
    private final MemberDetailsService memberDetailsService;
    private final AuthenticationManager authenticationManager;
    private final CheckStatusCode checkStatusCode;
    private final LoginLogService loginLogService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) throws AuthenticationException {
        LoginLog loginLog = new LoginLog();
        String browser = loginLogService.getloginBrowserByCode(loginRequest.getUserAgent());
        loginLog.setLoginBrowser(browser);
        System.out.println("로그인 사이트 유형 : " + loginRequest.getUserAgent());
        System.out.println("로그인 사이트 유형 : " + browser);
        try {
            // 사용자 인증
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getLoginId(), loginRequest.getLoginPw())
            );

            // 사용자 정보 로드
            final MemberDetails memberDetails = (MemberDetails) memberDetailsService.loadUserByUsername(loginRequest.getLoginId());
            // JWT 토큰 생성

            loginLog.setMemberNo(memberDetails.getMemberNo());
            loginLogService.logLoginAttempt(loginLog);

            System.out.println("memberDetails : " + memberDetails);

            final String jwt = jwtUtil.generateToken(memberDetails);
            final long expiresIn = jwtUtil.extractExpiration(jwt).getTime();
            final String refreshToken = jwtUtil.generateRefreshToken(memberDetails);

            System.out.println("checkStatusCode : " + checkStatusCode);
            checkStatusCode.checkStatus(memberDetails.getMemberNo(), response);
            System.out.println("checkStatusCode 실행");
            System.out.println("시간 : " + new Date(expiresIn));
            // 토큰을 포함한 응답 반환
            return ResponseEntity.ok(new LoginResponse("bearer", jwt, expiresIn, refreshToken, memberDetails.getMemberNo(), memberDetails.getPointNo(),null));

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("존재하지 않는 사용자 입니다."));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("잘못된 사용자이거나 비밀번호가 일치하지 않습니다."));
        } catch (Exception e) {
            int loginNumber =  loginLogService.getLoginNoByMemberNo(loginLog.getMemberNo());
            System.out.println("123123123 : " + loginNumber);
            loginLogService.logLoginFailure(loginNumber);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginResponse("로그인 중 오류가 발생했습니다."));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        //System.out.println("리프레시 작동");
        String refreshToken = request.getRefreshToken();
        //System.out.println("refreshToken : " + refreshToken);
        try {
            if (jwtUtil.validateToken(refreshToken)) {
                //System.out.println("if문 시작");
                String memberNo = jwtUtil.extractMemberNo(refreshToken);
                //System.out.println("memberNo : " + memberNo);
                MemberDetails memberDetails = (MemberDetails) memberDetailsService.loadUserByUsername(memberNo);
                //System.out.println("memberDetails : " + memberDetails);
                String newAccessToken = jwtUtil.generateToken(memberDetails);
                //System.out.println("newAccessToken : " + newAccessToken);
                long expiresIn = jwtUtil.extractExpiration(newAccessToken).getTime();
                //System.out.println("expiresIn : " + expiresIn);
                return ResponseEntity.ok(new RefreshTokenResponse(newAccessToken, expiresIn, refreshToken));
            } else {
                //System.out.println("111111111111");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 리프레시 토큰");
            }
        } catch (Exception e) {
            //System.out.println("2222222222222222");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 리프레시 토큰");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LoginRequest logoutRequest, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        int loginNo = loginLogService.getLoginNoByMemberNo(jwtUtil.extractMemberNo(token));
        loginLogService.logLogout(loginNo);
        // 추가적인 로그아웃 로직이 필요하면 여기서 처리
        return ResponseEntity.ok().body("로그아웃 성공");
    }
}

