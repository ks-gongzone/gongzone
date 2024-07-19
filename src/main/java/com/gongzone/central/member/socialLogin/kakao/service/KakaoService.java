package com.gongzone.central.member.socialLogin.kakao.service;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.domain.Token;
import com.gongzone.central.member.login.domain.LoginLog;
import com.gongzone.central.member.login.mapper.LoginMapper;
import com.gongzone.central.member.login.security.JwtUtil;
import com.gongzone.central.member.login.service.CheckStatusCode;
import com.gongzone.central.member.login.service.LoginLogService;
import com.gongzone.central.member.login.service.MemberDetails;
import com.gongzone.central.member.mapper.MemberMapper;
import com.gongzone.central.member.mapper.TokenMapper;
import com.gongzone.central.member.socialLogin.domain.SocialMember;
import com.gongzone.central.point.domain.Point;
import com.gongzone.central.point.mapper.PointMapper;
import com.gongzone.central.point.service.PointService;
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
public class KakaoService {

    private final MemberMapper memberMapper;
    private final TokenMapper tokenMapper;
    private final PointMapper pointMapper;
    private final PointService pointService;
    private final JwtUtil jwtUtil;
    private final Lock lock = new ReentrantLock();

    private final CheckStatusCode checkStatusCode;
    private final HttpServletResponse response;
    private final LoginLogService loginLogService;
    private final LoginMapper loginMapper;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String KAKAO_CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URI;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String KAKAO_TOKEN_URI;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String KAKAO_USER_INFO_URI;

    public Map<String, Object> kakaoToken(String code, String userAgent) throws Exception {
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
            accessTokenParams.add("client_id", KAKAO_CLIENT_ID);
            accessTokenParams.add("client_secret", KAKAO_CLIENT_SECRET);
            accessTokenParams.add("code", code);
            accessTokenParams.add("redirect_uri", KAKAO_REDIRECT_URI);

            HttpEntity<MultiValueMap<String, String>> accessTokenRequest = new HttpEntity<>(accessTokenParams, headers);
            ResponseEntity<String> accessTokenResponse = rt.exchange(
                    KAKAO_TOKEN_URI,
                    HttpMethod.POST,
                    accessTokenRequest,
                    String.class);

            JSONParser jsonParser = new JSONParser();
            String responseBody = accessTokenResponse.getBody();
            JSONObject parse = (JSONObject) jsonParser.parse(responseBody);

            String accessToken = (String) parse.get("access_token");
            String refreshToken = (String) parse.get("refresh_token");
            String expiresInStr = String.valueOf(parse.get("expires_in"));
            long expiresIn = Long.parseLong(expiresInStr);

            headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            HttpEntity<?> userRequest = new HttpEntity<>(headers);
            ResponseEntity<String> userResponse = rt.exchange(KAKAO_USER_INFO_URI, HttpMethod.GET, userRequest, String.class);
            responseBody = userResponse.getBody();
            parse = (JSONObject) jsonParser.parse(responseBody);

            JSONObject kakaoAccount = (JSONObject) parse.get("kakao_account");
            JSONObject profile = (JSONObject) kakaoAccount.get("profile");

            String name = (String) profile.get("nickname");
            String email = (String) kakaoAccount.get("email");
            String phoneNumber = "";
            String gender = (String) kakaoAccount.get("gender");

            SocialMember socialMember = new SocialMember();
            socialMember.setProvider("kakao");
            socialMember.setName(name);
            socialMember.setEmail(email);
            socialMember.setPhoneNumber(phoneNumber);
            socialMember.setGender(gender);
            socialMember.setAccessToken(accessToken);
            socialMember.setRefreshToken(refreshToken);
            socialMember.setAccessTokenExpiry(new Date(System.currentTimeMillis() + expiresIn * 1000));
            socialMember.setRefreshTokenExpiry(new Date(System.currentTimeMillis() + expiresIn * 1000 * 2));

            Member member = memberMapper.findByEmailFromToken(socialMember.getEmail());
            if (member == null) {
                result.put("socialMember", socialMember);
                result.put("isNewMember", true);
            } else {
                updateTokens(member.getMemberNo(), socialMember);
                checkStatusCode.checkStatus(member.getMemberNo(), response);

                Point point = pointService.getPoint(member.getMemberNo());
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
            int loginNo = loginMapper.loginNoByuserAgent(browser);
            loginLogService.logLoginFailure(loginNo);
            throw new Exception("카카오 로그인 중 오류 발생 : " + e.getMessage(), e);
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
}
