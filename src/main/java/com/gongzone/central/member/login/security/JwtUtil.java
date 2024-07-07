package com.gongzone.central.member.login.security;

import com.gongzone.central.member.login.service.MemberDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")     // 속성 값 주입
    private String secretKey;

    @Value("${jwt.expirationMs}")     // 유효 시간
    private int jwtExpirationMs;

    @Value("${jwt.refreshExpirationMs}")     // 리프레시 토큰 유효 시간
    private int jwtRefreshExpirationMs;

    private SecretKey key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성
    public String generateToken(MemberDetails memberDetails) {
        // 토큰에 값 넣는 곳
        Map<String, String> claims = new HashMap<>();
        claims.put("memberNo", memberDetails.getMemberNo());
        claims.put("pointNo", memberDetails.getPointNo());
        claims.put("email", memberDetails.getEmail());
        claims.put("memberId", memberDetails.getMemberId());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs); // jwtExpirationMs = 30000 (30초)

//        System.out.println("토큰생성");
//        System.out.println("발행 시간: " + now);
//        System.out.println("만료 시간: " + expiryDate);

//        System.out.println("토큰생성");
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(memberDetails.getMemberNo())
                .setIssuedAt(now)
                .setExpiration(expiryDate) // 토큰 유효 시간
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 클레임에서 값 추출
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsByToken(token);
        return claimsResolver.apply(claims);
    }

    // 모든 클레임
    public Claims getAllClaimsByToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 클레임 아이디
    public String extractMemberId(String token) {
        return extractClaim(token, claims -> claims.get("memberId", String.class));
    }

    // 클레임 이메일
    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    // 클레임 맴버번호
    public String extractMemberNo(String token) {
        return extractClaim(token, claims -> claims.get("memberNo", String.class));
    }

    // 클레임 포인트번호
    public String extractPointNo(String token) {
        return extractClaim(token, claims -> claims.get("pointNo", String.class));
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token, MemberDetails memberDetails) {
        final String memberNo = extractMemberNo(token);
        return (memberNo.equals(memberDetails.getMemberNo()) && !isTokenExpired(token));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            //System.out.println("검증 성공");
            return true;
        } catch (Exception e) {
            //System.out.println("검증 실패");
            e.printStackTrace();
            return false;
        }
    }

    // 리프레시 토큰
    public String generateRefreshToken(MemberDetails memberDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberNo", memberDetails.getMemberNo());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(memberDetails.getMemberNo())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 만료 날짜
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 토큰 만료확인
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
