package com.gongzone.central.member.socialLogin.naver.service;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.domain.Token;
import com.gongzone.central.member.login.security.JwtUtil;
import com.gongzone.central.member.login.service.CheckStatusCode;
import com.gongzone.central.member.login.service.MemberDetails;
import com.gongzone.central.member.mapper.MemberMapper;
import com.gongzone.central.member.mapper.TokenMapper;
import com.gongzone.central.member.socialLogin.naver.domain.SocialMember;
import com.gongzone.central.point.domain.Point;
import com.gongzone.central.point.mapper.PointMapper;
import com.gongzone.central.utils.MySqlUtil;
import jakarta.servlet.http.HttpServletResponse;
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
public class NaverService {

    private final MemberMapper memberMapper;
    private final TokenMapper tokenMapper;
    private final PointMapper pointMapper;
    private final JwtUtil jwtUtil;
    private final Lock lock = new ReentrantLock();
    private final CheckStatusCode checkStatusCode;
    private final HttpServletResponse response;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String NAVER_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String NAVER_CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String NAVER_REDIRECT_URI;

    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String NAVER_TOKEN_URI;

    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String NAVER_USER_INFO_URI;

    public SocialMember naverToken(String code) throws Exception {
        lock.lock();
        try {
            System.out.println("3");
            RestTemplate rt = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            //MultiValueMap<String, String> accessTokenParams = accessTokenParams("authorization_code", NAVER_CLIENT_SECRET, NAVER_CLIENT_ID, code, NAVER_REDIRECT_URI);
            MultiValueMap<String, String> accessTokenParams = new LinkedMultiValueMap<>();
            accessTokenParams.add("grant_type", "authorization_code");
            accessTokenParams.add("client_id", NAVER_CLIENT_ID);
            accessTokenParams.add("client_secret", NAVER_CLIENT_SECRET);
            accessTokenParams.add("code", code);
            accessTokenParams.add("redirect_uri", NAVER_REDIRECT_URI);

            HttpEntity<MultiValueMap<String, String>> accessTokenRequest = new HttpEntity<>(accessTokenParams, headers);
            System.out.println("accessTokenParams: " + accessTokenParams);
            ResponseEntity<String> accessTokenResponse = rt.exchange(
                    NAVER_TOKEN_URI,
                    HttpMethod.POST,
                    accessTokenRequest,
                    String.class);
            System.out.println("4");

            JSONParser jsonParser = new JSONParser();
            String responseBody = accessTokenResponse.getBody();
            System.out.println("네이버 응답" + responseBody);
            JSONObject parse = (JSONObject) jsonParser.parse(responseBody);
            System.out.println("parse " + parse.toString());
            System.out.println("5");

            String accessToken = (String) parse.get("access_token");
            String refreshToken = (String) parse.get("refresh_token");
            String expiresInStr = (String) parse.get("expires_in");
            long expiresIn = Long.parseLong(expiresInStr);
            System.out.println("6");

            headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            HttpEntity<?> userRequest = new HttpEntity<>(headers);
            ResponseEntity<String> userResponse = rt.exchange(NAVER_USER_INFO_URI, HttpMethod.GET, userRequest, String.class);
            responseBody = userResponse.getBody();
            System.out.println("userResponseBody: " + responseBody);
            parse = (JSONObject) jsonParser.parse(responseBody);
            System.out.println("7" + parse);

            JSONObject responseParse = (JSONObject) parse.get("response");
            //String socialId = (String) responseParse.get("id");
            String name = (String) responseParse.get("name");
            String email = (String) responseParse.get("email");
            String phoneNumber = (String) responseParse.get("mobile_e164");
            String gender = (String) responseParse.get("gender");
            System.out.println("8  ");

            SocialMember socialMember = new SocialMember();
            //socialMember.setSocialId(socialId);
            socialMember.setProvider("naver");
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
            System.out.println("member : " + member);
            if (member == null) {
                System.out.println("saveMember 실행" + member);
                member = saveMember(socialMember);
            } else {
                System.out.println("updateTokens 실행" + member);
                System.out.println("상태 코드 : " + member.getMemberStatus());
                System.out.println("checkStatusCode : " + checkStatusCode);
                System.out.println("response : " + response);
                checkStatusCode.checkStatus(member.getMemberNo(), response);
                System.out.println("checkStatusCode 실행");
                updateTokens(member.getMemberNo(), socialMember);
            }


            System.out.println("맴버 저장 " + member);
            System.out.println("9");


            // 회원 정보 가져오기
            //member = memberMapper.findByEmailFromToken(socialMember.getEmail());
            System.out.println("맴버 : " + member);
            Point point = pointMapper.getPointNoByMemberNo(member.getMemberNo());
            System.out.println("맴버 포인트 : " + point);
            // MemberDetails 객체 생성
            MemberDetails memberDetails = new MemberDetails(member, point);
            System.out.println("맴버 디테일 : " + memberDetails);

            // 자체 사이트 토큰 생성
            String siteToken = jwtUtil.generateToken(memberDetails);

            // SocialMember에 사이트 토큰 설정
            System.out.println("완료1  : " + siteToken);
            socialMember.setJwtToken(siteToken);
            socialMember.setMemberNo(member.getMemberNo());
            socialMember.setPointNo(point.getMemberPointNo());
            System.out.println("완료2  : " + siteToken);
            return socialMember;
        } catch (Exception e) {
            // 예외 처리
            throw new Exception("네이버 로그인 중 오류 발생 : " + e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }

    private Member saveMember(SocialMember socialMember) {
        String randomMemberId = UUID.randomUUID().toString();
        Member member = Member.builder()
                .memberNo("")  // 마지막값에 추가예정
                .memberId(randomMemberId)
                .memberName(socialMember.getName())
                .memberEmail(socialMember.getEmail())
                .memberPhone(socialMember.getPhoneNumber())
                .memberGender(socialMember.getGender())
                .build();

        memberMapper.insert(member);
        System.out.println("맴버 인서트 실행완 " + member);

        String lastPointNo = pointMapper.getLastMemberPointNo();
        String newPointNo = MySqlUtil.generatePrimaryKey(lastPointNo);

        Point point = Point.builder()
                .memberPointNo(newPointNo)
                .memberNo(member.getMemberNo())
                .memberPoint("0")
                .build();

        pointMapper.insertPoint(point);
        System.out.println("포인트 인서트 실행 :" + point);

        Token token = new Token();

        String lastTokenNo = tokenMapper.getLastTokenNo();
        int newTokenNo = Integer.parseInt(MySqlUtil.generatePrimaryKey(lastTokenNo));

        token.setTokenNo(newTokenNo);
        token.setMemberNo(member.getMemberNo());
        token.setTokenType(socialMember.getProvider());
        token.setTokenValueAcc(socialMember.getAccessToken());
        token.setTokenValueRef(socialMember.getRefreshToken());
        token.setTokenExpiresAcc(socialMember.getAccessTokenExpiry());
        token.setTokenExpiresRef(socialMember.getRefreshTokenExpiry());
        token.setTokenLastUpdate(new Date());

        tokenMapper.insert(token);
        System.out.println("토큰 인서트 실행완 " + token);
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
            token.setTokenValueRef(socialMember.getRefreshToken());
            token.setTokenExpiresAcc(socialMember.getAccessTokenExpiry());
            token.setTokenExpiresRef(socialMember.getRefreshTokenExpiry());
            token.setTokenLastUpdate(new Date());

            tokenMapper.insert(token);
            System.out.println("토큰 인서트 실행완 " + token);
        } else {
            token.setTokenValueAcc(socialMember.getAccessToken());
            token.setTokenValueRef(socialMember.getRefreshToken());
            token.setTokenExpiresAcc(socialMember.getAccessTokenExpiry());
            token.setTokenExpiresRef(socialMember.getRefreshTokenExpiry());
            token.setTokenLastUpdate(new Date());

            tokenMapper.update(token);
            System.out.println("토큰 업데이트 실행완 " + token);
        }
    }

    private MultiValueMap<String, String> accessTokenParams(String grantType, String clientSecret, String clientId, String code, String redirect_uri) {
        MultiValueMap<String, String> accessTokenParams = new LinkedMultiValueMap<>();
        accessTokenParams.add("grant_type", grantType);
        accessTokenParams.add("client_id", clientId);
        accessTokenParams.add("client_secret", clientSecret);
        accessTokenParams.add("code", code);
        accessTokenParams.add("redirect_uri", redirect_uri);

        System.out.println("accessTokenParams 실행 " + accessTokenParams);
        return accessTokenParams;
    }
}
