package com.gongzone.central.member.myInfo.service;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.myInfo.domain.MyInformation;
import com.gongzone.central.member.myInfo.mapper.PasswordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @제목: 내정보 서비스 구현
 * @생성일: 2024-06-18
 * @수정일: 2024-06-19
 * @내용: 비밀번호 입력 전 유저 존재 유무 및 신규 비밀번호 유효성 검증
 */
@Service
public class MyInfoServiceImpl implements MyInfoService {

    private final PasswordMapper passwordMapper;

    @Autowired
    public MyInfoServiceImpl(PasswordMapper passwordMapper) {
        this.passwordMapper = passwordMapper;
    }

    @Override
    public void updatePassword(Member member, MyInformation myInformation) {
        String newPassword = myInformation.getNewPassword();
        Member existMember = passwordMapper.findByNo(member.getMemberNo());

        if (existMember == null) {
            throw new RuntimeException("해당 유저가 존재하지 않습니다.");
        }
        // 소셜로그인 회원 예외처리
        if (!StringUtils.hasText(member.getMemberId()) || !StringUtils.hasText(existMember.getMemberPw())) {
            throw new RuntimeException("소셜로그인 회원은 비밀번호 수정 불가합니다.");
        }
        if (!existMember.getMemberPw().equals(member.getMemberPw())) {
            throw  new RuntimeException("현재 비밀번호가 다릅니다.");
        }
        if (newPassword.equals(member.getMemberPw())) {
            throw new RuntimeException("현재 사용중인 비밀번호와 같습니다.");
        }
        passwordMapper.updatePassword(existMember.getMemberNo(), newPassword);
    }

    // mapper에서 String 타입으로 받기로 해서 String 선언
    @Override
    public Member findByNo(String memberNo) {
        return passwordMapper.findByNo(memberNo);
    }
}
