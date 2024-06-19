package com.gongzone.central.member.myInfo.controller;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.myInfo.domain.MyInformation;
import com.gongzone.central.member.myInfo.service.MyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/myPage")
public class MyInfoController {
    private final MyInfoService myInfoService;
    @Autowired
    public MyInfoController(MyInfoService myInfoService) {
        this.myInfoService = myInfoService;
    }

    /**
     * @제목 : 사용자 세션 값 관리
     * @작성일: 2024-06-19
     * @수정일: 2024-06-19
     * @내용: 세션의 존재에 따라 1 또는 0반환 후 로직 처리 기대
     */
    @GetMapping("/{memberNo}/session")
    public String getSessionMember(@PathVariable String memberNo) {
        Member member = myInfoService.findByNo(memberNo);
        return (member != null) ? "1" : "0";
    }

    /**
     * @제목: 특정회원 정보 세션
     * @작성일: 2024-06-19
     * @수정일: 2024-06-19
     * @내용: 로그인 된 특정회원 정보 return
     */
    @GetMapping("/{memberNo}/memberInfo")
    public Member getMemberInfo(@PathVariable String memberNo) {
        Member member = myInfoService.findByNo(memberNo);
        if (member != null) {
            return member;
        } else {
            throw new RuntimeException("회원 정보를 가져올 수 없습니다.");
        }
    }

    @PostMapping("/{memberNo}/password")
    public String updatePassword(@PathVariable String memberNo, @RequestBody MyInformation myInformation) {
        Member member = myInfoService.findByNo(memberNo);
        if (member == null) {
            return "로그인된 사용자가 없습니다.";
        }
        try {
            myInfoService.updatePassword(member, myInformation);
            return "비밀번호 변경 성공.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}
