package com.gongzone.central.member.socialLogin.naver.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Transactional
@RequiredArgsConstructor
public class NaverService {

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

    public Map<String, Object> naverToken(String code, String userAgent) throws Exception {
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
            accessTokenParams.add("client_id", NAVER_CLIENT_ID);
            accessTokenParams.add("client_secret", NAVER_CLIENT_SECRET);
            accessTokenParams.add("code", code);
            accessTokenParams.add("redirect_uri", NAVER_REDIRECT_URI);

            HttpEntity<MultiValueMap<String, String>> accessTokenRequest = new HttpEntity<>(accessTokenParams, headers);
            ResponseEntity<String> accessTokenResponse = rt.exchange(
                    NAVER_TOKEN_URI,
                    HttpMethod.POST,
                    accessTokenRequest,
                    String.class);

            JSONParser jsonParser = new JSONParser();
            String responseBody = accessTokenResponse.getBody();
            JSONObject parse = (JSONObject) jsonParser.parse(responseBody);

            String socialAccessToken = (String) parse.get("access_token");
            String socialRefreshToken = (String) parse.get("refresh_token");
            String expiresInStr = (String) parse.get("expires_in");
            long expiresIn = Long.parseLong(expiresInStr);

            headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + socialAccessToken);
            HttpEntity<?> userRequest = new HttpEntity<>(headers);
            ResponseEntity<String> userResponse = rt.exchange(NAVER_USER_INFO_URI, HttpMethod.GET, userRequest, String.class);
            responseBody = userResponse.getBody();
            parse = (JSONObject) jsonParser.parse(responseBody);

            JSONObject responseParse = (JSONObject) parse.get("response");
            String name = (String) responseParse.get("name");
            String email = (String) responseParse.get("email");
            String phoneNumber = (String) responseParse.get("mobile_e164");
            String gender = (String) responseParse.get("gender");

            SocialMember socialMember = new SocialMember();
            socialMember.setProvider("naver");
            socialMember.setName(name);
            socialMember.setEmail(email);
            socialMember.setPhoneNumber(phoneNumber);
            socialMember.setGender(gender);
            socialMember.setSocialAccessToken(socialAccessToken);
            socialMember.setSocialRefreshToken(socialRefreshToken);
            socialMember.setAccessTokenExpiry(new Date(System.currentTimeMillis() + expiresIn * 1000));
            socialMember.setRefreshTokenExpiry(new Date(System.currentTimeMillis() + expiresIn * 1000 * 2));

            Member member = memberMapper.findByEmailFromToken(socialMember.getEmail());

            if (member == null) {
                member = saveMember(socialMember);
                result.put("socialMember", socialMember);
            } else {
                checkStatusCode.checkStatus(member.getMemberNo(), response);
                updateTokens(member.getMemberNo(), socialMember);
            }

            Point point = pointService.getPoint(member.getMemberNo());
            MemberDetails memberDetails = new MemberDetails(member, point);
            String siteToken = jwtUtil.generateToken(memberDetails);
            String siteRefreshToken = jwtUtil.generateRefreshToken(memberDetails);
            long siteExpiresIn = jwtUtil.extractExpiration(siteToken).getTime();

            socialMember.setAccessToken(siteToken);
            socialMember.setRefreshToken(siteRefreshToken);
            socialMember.setMemberNo(member.getMemberNo());
            socialMember.setPointNo(point.getMemberPointNo());
            socialMember.setTokenExpiresIn(siteExpiresIn);

            loginLog.setMemberNo(socialMember.getMemberNo());
            loginLogService.logLoginAttempt(loginLog);

            result.put("socialMember", socialMember);

            return result;
        } catch (Exception e) {
            int loginNo = loginMapper.loginNoByuserAgent(browser);
            loginLogService.logLoginFailure(loginNo);
            throw new Exception("네이버 로그인 중 오류 발생 : " + e.getMessage(), e);
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

        String lastPointNo = pointMapper.getLastIndex();
        String newPointNo = MySqlUtil.generatePrimaryKey(lastPointNo);

        Point point = Point.builder()
                .memberPointNo(newPointNo)
                .memberNo(member.getMemberNo())
                .memberPoint("0")
                .build();

        pointMapper.insert(point);

        Token token = new Token();

        String lastTokenNo = tokenMapper.getLastTokenNo();
        int newTokenNo = Integer.parseInt(MySqlUtil.generatePrimaryKey(lastTokenNo));

        token.setTokenNo(newTokenNo);
        token.setMemberNo(member.getMemberNo());
        token.setTokenType(socialMember.getProvider());
        token.setTokenValueAcc(socialMember.getSocialAccessToken());
        token.setTokenValueRef(socialMember.getSocialRefreshToken());
        token.setTokenExpiresAcc(socialMember.getAccessTokenExpiry());
        token.setTokenExpiresRef(socialMember.getRefreshTokenExpiry());
        token.setTokenLastUpdate(new Date());

        tokenMapper.insert(token);
        return member;
    }

    private void updateTokens(String memberNo, SocialMember socialMember) {
        Token token = tokenMapper.findByMemberNo(memberNo);
        if (token == null) {
            token = new Token();
            token.setMemberNo(memberNo);
            token.setTokenType(socialMember.getProvider());
            token.setTokenValueAcc(socialMember.getSocialAccessToken());
            token.setTokenValueRef(socialMember.getSocialRefreshToken());
            token.setTokenExpiresAcc(socialMember.getAccessTokenExpiry());
            token.setTokenExpiresRef(socialMember.getRefreshTokenExpiry());
            token.setTokenLastUpdate(new Date());

            tokenMapper.insert(token);
        } else {
            token.setTokenValueAcc(socialMember.getSocialAccessToken());
            token.setTokenValueRef(socialMember.getSocialRefreshToken());
            token.setTokenExpiresAcc(socialMember.getAccessTokenExpiry());
            token.setTokenExpiresRef(socialMember.getRefreshTokenExpiry());
            token.setTokenLastUpdate(new Date());

            tokenMapper.update(token);
        }
    }
}
