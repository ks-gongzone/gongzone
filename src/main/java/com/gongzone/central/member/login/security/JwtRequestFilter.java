package com.gongzone.central.member.login.security;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.login.service.MemberDetails;
import com.gongzone.central.member.login.service.MemberDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final MemberDetailsService memberDetailsService;

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // 토큰 추출후 검증
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwt = requestTokenHeader.substring(7);
            try {
                username = jwtUtil.extractMemberNo(jwt);

                // 받은 후 유효성 검사
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    System.out.println("유효성 검사 시작" + username);
                    MemberDetails memberDetails = (MemberDetails) this.memberDetailsService.loadUserByUsername(username);
                    System.out.println("유효성 끝: " + memberDetails.getUsername());
                    System.out.println("jwt: " + jwt);
                    System.out.println("userDetails: " + memberDetails);
                    // 토큰이 유효한지 검사
                    if (jwtUtil.validateToken(jwt, memberDetails)) {
                        System.out.println("진행 if문" + memberDetails);
                        memberDetails.setToken(jwt);
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());

                        usernamePasswordAuthenticationToken
                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        System.out.println("Au 인증 " + usernamePasswordAuthenticationToken);
                        System.out.println("if문 완료: " + memberDetails);
                    }
                }
           } catch (IllegalArgumentException e) {
                System.out.println("토큰이 없습니다");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }
        chain.doFilter(request, response);
    }

    /*@Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return "/api/login".equals(request.getRequestURI());
    }*/
}
