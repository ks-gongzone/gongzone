package com.gongzone.central.member.socialLogin.service;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.domain.Token;
import com.gongzone.central.member.login.security.JwtUtil;
import com.gongzone.central.member.mapper.MemberMapper;
import com.gongzone.central.member.mapper.TokenMapper;
import com.gongzone.central.member.socialLogin.domain.SocialMember;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class NaverService {

    private final MemberMapper memberMapper;
    private final TokenMapper tokenMapper;
    private final JwtUtil jwtUtil;

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
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        MultiValueMap<String, String> accessTokenParams = accessTokenParams("authorization_code", NAVER_CLIENT_SECRET, NAVER_CLIENT_ID, code, NAVER_REDIRECT_URI);
        HttpEntity<MultiValueMap<String, String>> accessTokenRequest = new HttpEntity<>(accessTokenParams, headers);
        ResponseEntity<String> accessTokenResponse = rt.exchange(
                NAVER_TOKEN_URI,
                HttpMethod.POST,
                accessTokenRequest,
                String.class);

        JSONParser jsonParser = new JSONParser();
        String responseBody = accessTokenResponse.getBody();
        JSONObject parse = (JSONObject) jsonParser.parse(responseBody);

        String accessToken = (String) parse.get("access_token");
        String refreshToken = (String) parse.get("refresh_token");
        long expiresIn = (Long) parse.get("expires_in");

        headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity<?> userRequest = new HttpEntity<>(headers);
        ResponseEntity<String> userResponse = rt.exchange(NAVER_USER_INFO_URI, HttpMethod.GET, userRequest, String.class);
        responseBody = userResponse.getBody();
        parse = (JSONObject) jsonParser.parse(responseBody);

        JSONObject responseParse = (JSONObject) parse.get("response");
        String socialId = (String) responseParse.get("id");
        String name = (String) responseParse.get("name");
        String email = (String) responseParse.get("email");
        String phoneNumber = (String) responseParse.get("mobile_e164");
        String gender = (String) responseParse.get("gender");

        SocialMember socialMember = new SocialMember();
        socialMember.setSocialId(socialId);
        socialMember.setProvider("naver");
        socialMember.setName(name);
        socialMember.setEmail(email);
        socialMember.setPhoneNumber(phoneNumber);
        socialMember.setGender(gender);
        socialMember.setAccessToken(accessToken);
        socialMember.setRefreshToken(refreshToken);
        socialMember.setAccessTokenExpiry(new Date(System.currentTimeMillis() + expiresIn * 1000));
        socialMember.setRefreshTokenExpiry(new Date(System.currentTimeMillis() + expiresIn * 1000 * 2));

        saveMember(socialMember);

        return socialMember;
    }

    private void saveMember(SocialMember socialMember) {
        Member member = memberMapper.findByIdFromToken(socialMember.getSocialId());
        if (member == null) {
            member = new Member();
            member.builder()
                            .memberNo("")  // 마지막값에 추가예정
                            .memberId(socialMember.getSocialId())
                            .memberName(socialMember.getName())
                            .memberEmail(socialMember.getEmail())
                            .memberPhone(socialMember.getPhoneNumber())
                            .memberGender(socialMember.getGender());

            memberMapper.insert(member);
        }

        Token token = tokenMapper.findByMemberNo(member.getMemberNo());
        if (token == null) {
            token = new Token();
            token.setMemberNo(member.getMemberNo());
            token.setTokenType("Bearer");
            token.setTokenValueAcc(socialMember.getAccessToken());
            token.setTokenValueRef(socialMember.getRefreshToken());
            token.setTokenExpiresAcc(socialMember.getAccessTokenExpiry());
            token.setTokenExpiresRef(socialMember.getRefreshTokenExpiry());
            token.setTokenLastUpdate(new Date());

            tokenMapper.insert(token);
        } else {
            token.setTokenValueAcc(socialMember.getAccessToken());
            token.setTokenValueRef(socialMember.getRefreshToken());
            token.setTokenExpiresAcc(socialMember.getAccessTokenExpiry());
            token.setTokenExpiresRef(socialMember.getRefreshTokenExpiry());
            token.setTokenLastUpdate(new Date());

            tokenMapper.update(token);
        }
    }

    private MultiValueMap<String, String> accessTokenParams(String grantType, String clientSecret, String clientId, String code, String redirect_uri) {
        MultiValueMap<String, String> accessTokenParams = new LinkedMultiValueMap<>();
        accessTokenParams.add("grant_type", grantType);
        accessTokenParams.add("client_id", clientId);
        accessTokenParams.add("client_secret", clientSecret);
        accessTokenParams.add("code", code);
        accessTokenParams.add("redirect_uri", redirect_uri);
        return accessTokenParams;
    }
}


