package com.gongzone.central.member.login.security;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.login.service.MemberDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")     // 속성 값 주입
    private String SECRET_KEY;

    public String generateToken(MemberDetails memberDetails) {
        // 토큰에 값 넣는 곳
        Member member = memberDetails.getMember();
        String memberNo = member.getMemberNo();
        Map<String, String> claims = new HashMap<>();
        claims.put("memberNo", memberNo);
        return Jwts.builder()
                .setSubject(memberDetails.getUsername())
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 토큰 유효 시간
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 여기서 토큰값 담지말고 주스탠드를 써서 담아서 써라
    // 원하는 토큰값 Claims를 조회하는곳
    public Claims getJwtClaimsNo(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, MemberDetails memberDetails) {
        final String username = extractUsername(token);
        return (username.equals(memberDetails.getUsername()) && !isTokenExpired(token));
    }

    public Date getExpirationDateFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    private boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }
}
