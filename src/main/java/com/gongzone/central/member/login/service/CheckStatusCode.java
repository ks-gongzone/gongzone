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
        System.out.println("55555555555555555555");
         Member member = memberService.getMemberByStatus(memberNo);
         StatusCode statusCode = StatusCode.fromCode(member.getMemberStatus());
        System.out.println("666666666666666666666666");
            switch (statusCode) {
            case S010102:
                System.out.println("휴면");
                response.sendError(HttpServletResponse.SC_GONE, "휴면");
                return;
            case S010103:
                System.out.println("제재");
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "제재");
                return;
            case S010104:
                System.out.println("탈퇴");
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "탈퇴");
                return;
            default:    // 정상상태
                System.out.println("정상");
                break;
        }
    }
}
