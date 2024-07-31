package com.gongzone.central.member.login.service;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.service.MemberService;
import com.gongzone.central.utils.StatusCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CheckStatusCode {

    private final MemberService memberService;

    public void checkStatus(String memberNo, HttpServletResponse response) throws IOException {
        Member member = memberService.getMemberByStatus(memberNo);
        StatusCode statusCode = StatusCode.fromCode(member.getMemberStatus());
        switch (statusCode) {
            case S010102:
                response.sendError(HttpServletResponse.SC_GONE, "휴면");
                return;
            case S010103:
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "제재");
                return;
            case S010104:
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "탈퇴");
                return;
            default:    // 정상상태
                break;
        }
    }
}
