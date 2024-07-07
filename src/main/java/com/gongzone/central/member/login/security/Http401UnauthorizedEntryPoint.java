package com.gongzone.central.member.login.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Http401UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    /*@Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        if (request.getAttribute("exceptionRefreshToken") instanceof ExpiredJwtException) {
            System.out.println("exceptionRefreshToken 메서드 실행");
            response.setHeader("token-expired", "true");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token has expired");
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }*/
}
