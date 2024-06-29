package com.gongzone.central.member.socialLogin.google.service;


import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.domain.Token;
import com.gongzone.central.member.login.security.JwtUtil;
import com.gongzone.central.member.login.service.MemberDetails;
import com.gongzone.central.member.mapper.MemberMapper;
import com.gongzone.central.member.mapper.TokenMapper;
import com.gongzone.central.member.socialLogin.naver.domain.SocialMember;
import com.gongzone.central.point.domain.Point;
import com.gongzone.central.point.mapper.PointMapper;
import com.gongzone.central.utils.MySqlUtil;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
@EnableScheduling
@Transactional
public class GoogleService {

    private final MemberMapper memberMapper;
    private final TokenMapper tokenMapper;
    private final PointMapper pointMapper;
    private final JwtUtil jwtUtil;
    private final Lock lock = new ReentrantLock();

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String GOOGLE_REDIRECT_URI;

    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String GOOGLE_TOKEN_URI;

    @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
    private String GOOGLE_USER_INFO_URI;

    public SocialMember googleToken(String code) throws Exception {
        lock.lock();
        try {
            System.out.println("3");
            RestTemplate rt = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            MultiValueMap<String, String> accessTokenParams = new LinkedMultiValueMap<>();
            accessTokenParams.add("grant_type", "authorization_code");
            accessTokenParams.add("client_id", GOOGLE_CLIENT_ID);
            accessTokenParams.add("client_secret", GOOGLE_CLIENT_SECRET);
            accessTokenParams.add("code", code);
            accessTokenParams.add("redirect_uri", GOOGLE_REDIRECT_URI);

            HttpEntity<MultiValueMap<String, String>> accessTokenRequest = new HttpEntity<>(accessTokenParams, headers);
            System.out.println("accessTokenParams: " + accessTokenParams);
            ResponseEntity<String> accessTokenResponse = rt.exchange(
                    GOOGLE_TOKEN_URI,
                    HttpMethod.POST,
                    accessTokenRequest,
                    String.class);
            System.out.println("4");

            JSONParser jsonParser = new JSONParser();
            String responseBody = accessTokenResponse.getBody();
            System.out.println("구글 응답 : "+ responseBody);
            JSONObject parse = (JSONObject) jsonParser.parse(responseBody);
            System.out.println("parse : " + parse);

            String accessToken = (String) parse.get("access_token");
            String refreshToken = (String) parse.get("refresh_token");
            //String expiresInStr = (String) parse.get("expires_in");
            Long expiresIn = (Long) parse.get("expires_in");

            headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            HttpEntity<?> userRequest = new HttpEntity<>(headers);
            ResponseEntity<String> userResponse = rt.exchange(GOOGLE_USER_INFO_URI, HttpMethod.GET, userRequest, String.class);
            responseBody = userResponse.getBody();
            System.out.println("userResponseBody: " + responseBody);
            parse = (JSONObject) jsonParser.parse(responseBody);
            System.out.println("7" + parse);

            JSONObject responseParse = (JSONObject) parse.get("response");
            String name = (String) parse.get("name");
            String email = (String) parse.get("email");
            String phoneNumber = (String) parse.get("phoneNumber");
            String gender = (String) parse.get("gender");
            System.out.println("8  ");

            SocialMember socialMember = new SocialMember();
            socialMember.setProvider("google");
            socialMember.setName(name);
            socialMember.setEmail(email);
            socialMember.setPhoneNumber(phoneNumber);
            socialMember.setGender(gender);
            socialMember.setAccessToken(accessToken);
            socialMember.setRefreshToken(refreshToken);
            socialMember.setAccessTokenExpiry(new Date(System.currentTimeMillis() + expiresIn * 1000));
            socialMember.setRefreshTokenExpiry(new Date(System.currentTimeMillis() + expiresIn * 1000 * 2));

            System.out.println("socialMember123123123 : " + memberMapper.findByEmailFromToken(socialMember.getEmail()));
            System.out.println("socialMember : " + socialMember.getEmail());
            System.out.println("socialMember123123123 : " + memberMapper.findByEmailFromToken(socialMember.getEmail()));
            Member member = memberMapper.findByEmailFromToken(socialMember.getEmail());
            if (member == null) {
                System.out.println("saveMember 실행" + member);
                member = saveMember(socialMember);
            } else {
                System.out.println("updateTokens 실행" + member);
                updateTokens(member.getMemberNo(), socialMember);
            }

            System.out.println("맴버 저장 " + member);
            System.out.println("9");


            Point point = pointMapper.getPointNoByMemberNo(member.getMemberNo());
            System.out.println("맴버 포인트 : " + point);

            MemberDetails memberDetails = new MemberDetails(member, point);
            System.out.println("맴버 디테일 : " + memberDetails);
            String siteToken = jwtUtil.generateToken(memberDetails);

            System.out.println("완료1  : " + siteToken);
            socialMember.setJwtToken(siteToken);
            socialMember.setMemberNo(member.getMemberNo());
            socialMember.setPointNo(point.getMemberPointNo());
            System.out.println("완료2  : " + siteToken);

            return socialMember;
        } catch (Exception e) {
            throw new Exception("구글 로그인 중 오류 발생 : " + e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }

    private Member saveMember(SocialMember socialMember) {
        String randomMemberId = UUID.randomUUID().toString();
        Member member = Member.builder()
                .memberNo("")
                .memberId(randomMemberId)
                .memberName(socialMember.getName())
                .memberEmail(socialMember.getEmail())
                .memberPhone(socialMember.getPhoneNumber())
                .memberGender(socialMember.getGender())
                .build();

        memberMapper.insert(member);

        String lastPointNo = pointMapper.getLastMemberPointNo();
        String newPointNo = MySqlUtil.generatePrimaryKey(lastPointNo);

        Point point = Point.builder()
                .memberPointNo(newPointNo)
                .memberNo(member.getMemberNo())
                .memberPoint("0")
                .build();

        pointMapper.insertPoint(point);

        Token token = new Token();

        String lastTokenNo = tokenMapper.getLastTokenNo();
        int newTokenNo = Integer.parseInt(MySqlUtil.generatePrimaryKey(lastTokenNo));

        token.setTokenNo(newTokenNo);
        token.setMemberNo(member.getMemberNo());
        token.setTokenType(socialMember.getProvider());
        token.setTokenValueAcc(socialMember.getAccessToken());
        token.setTokenValueRef(socialMember.getRefreshToken() != null ? socialMember.getRefreshToken() : "null");
        token.setTokenExpiresAcc(socialMember.getAccessTokenExpiry());
        token.setTokenExpiresRef(socialMember.getRefreshTokenExpiry() != null ? socialMember.getRefreshTokenExpiry() : new Date(System.currentTimeMillis() + 24L * 60L * 60L * 1000L));
        token.setTokenLastUpdate(new Date());;

        tokenMapper.insert(token);

        return member;
    }

    private void updateTokens(String memberNo, SocialMember socialMember) {
        System.out.println("업데이트 실행");
        Token token = tokenMapper.findByMemberNo(memberNo);
        if (token == null) {
            token = new Token();
            token.setMemberNo(memberNo);
            token.setTokenType(socialMember.getProvider());
            token.setTokenValueAcc(socialMember.getAccessToken());
            token.setTokenValueRef(socialMember.getRefreshToken() != null ? socialMember.getRefreshToken() : "null");
            token.setTokenExpiresAcc(socialMember.getAccessTokenExpiry());
            token.setTokenExpiresRef(socialMember.getRefreshTokenExpiry() != null ? socialMember.getRefreshTokenExpiry() : new Date(System.currentTimeMillis() + 24L * 60L * 60L * 1000L));
            token.setTokenLastUpdate(new Date());

            tokenMapper.insert(token);
            System.out.println("토큰 인서트 실행완 " + token);
        } else {
            token.setTokenValueAcc(socialMember.getAccessToken());
            token.setTokenValueRef(socialMember.getRefreshToken() != null ? socialMember.getRefreshToken() : token.getTokenValueRef());
            token.setTokenExpiresAcc(socialMember.getAccessTokenExpiry());
            token.setTokenExpiresRef(socialMember.getRefreshTokenExpiry() != null ? socialMember.getRefreshTokenExpiry() : token.getTokenExpiresRef());
            token.setTokenLastUpdate(new Date());

            tokenMapper.update(token);
            System.out.println("토큰 업데이트 실행완 " + token);
        }
    }
}

