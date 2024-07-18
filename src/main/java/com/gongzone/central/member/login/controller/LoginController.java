package com.gongzone.central.member.login.controller;

import com.gongzone.central.member.login.domain.*;
import com.gongzone.central.member.login.security.JwtUtil;
import com.gongzone.central.member.login.service.CheckStatusCode;
import com.gongzone.central.member.login.service.LoginLogService;
import com.gongzone.central.member.login.service.MemberDetails;
import com.gongzone.central.member.login.service.MemberDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
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
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getLoginId(), loginRequest.getLoginPw())
            );

            final MemberDetails memberDetails = (MemberDetails) memberDetailsService.loadUserByUsername(loginRequest.getLoginId());

            loginLog.setMemberNo(memberDetails.getMemberNo());
            loginLogService.logLoginAttempt(loginLog);

            final String jwt = jwtUtil.generateToken(memberDetails);
            final long expiresIn = jwtUtil.extractExpiration(jwt).getTime();
            final String refreshToken = jwtUtil.generateRefreshToken(memberDetails);

            checkStatusCode.checkStatus(memberDetails.getMemberNo(), response);

            return ResponseEntity.ok(new LoginResponse("bearer", jwt, expiresIn, refreshToken, memberDetails.getMemberNo(), memberDetails.getPointNo(), null));

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("존재하지 않는 사용자 입니다."));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("잘못된 사용자이거나 비밀번호가 일치하지 않습니다."));
        } catch (Exception e) {
            LoginLog loginNumber = loginLogService.getLoginNoByMemberNo(loginLog.getMemberNo(), loginLog.getUserAgent());
            loginLogService.logLoginFailure(loginNumber.getLoginNo());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginResponse("로그인 중 오류가 발생했습니다."));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        try {
            if (jwtUtil.validateToken(refreshToken)) {
                String memberNo = jwtUtil.extractMemberNo(refreshToken);
                MemberDetails memberDetails = (MemberDetails) memberDetailsService.loadUserByUsername(memberNo);
                String newAccessToken = jwtUtil.generateToken(memberDetails);
                long expiresIn = jwtUtil.extractExpiration(newAccessToken).getTime();

                return ResponseEntity.ok(new RefreshTokenResponse(newAccessToken, expiresIn, refreshToken));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 리프레시 토큰");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 리프레시 토큰");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LoginRequest logoutRequest, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        LoginLog loginNo = loginLogService.getLoginNoByMemberNo(jwtUtil.extractMemberNo(token), logoutRequest.getUserAgent());
        loginLogService.logLogout(loginNo.getLoginNo());

        return ResponseEntity.ok().body("로그아웃 성공");
    }

    @PostMapping("/admin/statisticalDate/login")
    public List<LoginStatistical> adminLoginStatisticalDate() {
        List<LoginStatistical> loginDate = loginLogService.getLoginStatisticalDate();
        return loginDate;
    }

}

