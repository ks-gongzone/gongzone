package com.gongzone.central.member.socialLogin.google.service;


import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.domain.Token;
import com.gongzone.central.member.login.domain.LoginLog;
import com.gongzone.central.member.login.security.JwtUtil;
import com.gongzone.central.member.login.service.CheckStatusCode;
import com.gongzone.central.member.login.service.LoginLogService;
import com.gongzone.central.member.login.service.MemberDetails;
import com.gongzone.central.member.mapper.MemberMapper;
import com.gongzone.central.member.mapper.TokenMapper;
import com.gongzone.central.member.socialLogin.domain.SocialMember;
import com.gongzone.central.point.domain.Point;
import com.gongzone.central.point.mapper.PointMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
@Transactional
public class GoogleService {

    private final MemberMapper memberMapper;
    private final TokenMapper tokenMapper;
    private final PointMapper pointMapper;
    private final JwtUtil jwtUtil;
    private final Lock lock = new ReentrantLock();

    private final CheckStatusCode checkStatusCode;
    private final HttpServletResponse response;
    private final LoginLogService loginLogService;

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

    public Map<String, Object> googleToken(String code, String userAgent) throws Exception {
        lock.lock();
        Map<String, Object> result = new HashMap<>();

        LoginLog loginLog = new LoginLog();
        String browser = loginLogService.getloginBrowserByCode(userAgent);
        loginLog.setLoginBrowser(browser);

        try {
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
            ResponseEntity<String> accessTokenResponse = rt.exchange(
                    GOOGLE_TOKEN_URI,
                    HttpMethod.POST,
                    accessTokenRequest,
                    String.class);

            JSONParser jsonParser = new JSONParser();
            String responseBody = accessTokenResponse.getBody();
            JSONObject parse = (JSONObject) jsonParser.parse(responseBody);

            String accessToken = (String) parse.get("access_token");
            String refreshToken = (String) parse.get("refresh_token");
            Long expiresIn = (Long) parse.get("expires_in");

            headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            HttpEntity<?> userRequest = new HttpEntity<>(headers);
            ResponseEntity<String> userResponse = rt.exchange(GOOGLE_USER_INFO_URI, HttpMethod.GET, userRequest, String.class);
            responseBody = userResponse.getBody();
            parse = (JSONObject) jsonParser.parse(responseBody);

            String name = (String) parse.get("name");
            String email = (String) parse.get("email");

            SocialMember socialMember = new SocialMember();
            socialMember.setProvider("google");
            socialMember.setName(name);
            socialMember.setEmail(email);
            socialMember.setAccessToken(accessToken);
            socialMember.setRefreshToken(refreshToken);
            socialMember.setAccessTokenExpiry(new Date(System.currentTimeMillis() + expiresIn * 1000));
            socialMember.setRefreshTokenExpiry(new Date(System.currentTimeMillis() + expiresIn * 1000 * 2));

            Member member = memberMapper.findByEmailFromToken(socialMember.getEmail());
            if (member == null) {
                result.put("socialMember", socialMember);
                result.put("isNewMember", true);
            } else {
                checkStatusCode.checkStatus(member.getMemberNo(), response);
                
                updateTokens(member.getMemberNo(), socialMember);

                Point point = pointMapper.getPointNoByMemberNo(member.getMemberNo());
                MemberDetails memberDetails = new MemberDetails(member, point);
                String siteToken = jwtUtil.generateToken(memberDetails);

                socialMember.setJwtToken(siteToken);
                socialMember.setMemberNo(member.getMemberNo());
                socialMember.setPointNo(point.getMemberPointNo());

                loginLog.setMemberNo(socialMember.getMemberNo());
                loginLogService.logLoginAttempt(loginLog);

                result.put("socialMember", socialMember);
                result.put("isNewMember", false);
            }
            return result;
        } catch (Exception e) {
            LoginLog loginNumber =  loginLogService.getLoginNoByMemberNo(loginLog.getMemberNo(), loginLog.getUserAgent());
            loginLogService.logLoginFailure(loginNumber.getLoginNo());
            throw new Exception("구글 로그인 중 오류 발생 : " + e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }

    private void updateTokens(String memberNo, SocialMember socialMember) {
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
        } else {
            token.setTokenValueAcc(socialMember.getAccessToken());
            token.setTokenValueRef(socialMember.getRefreshToken() != null ? socialMember.getRefreshToken() : token.getTokenValueRef());
            token.setTokenExpiresAcc(socialMember.getAccessTokenExpiry());
            token.setTokenExpiresRef(socialMember.getRefreshTokenExpiry() != null ? socialMember.getRefreshTokenExpiry() : token.getTokenExpiresRef());
            token.setTokenLastUpdate(new Date());

            tokenMapper.update(token);
        }
    }
}

