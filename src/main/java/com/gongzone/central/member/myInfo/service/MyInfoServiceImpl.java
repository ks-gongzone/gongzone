package com.gongzone.central.member.myInfo.service;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.myInfo.domain.MyInformation;
import com.gongzone.central.member.myInfo.mapper.MyInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @제목: 내정보 서비스 구현
 * @생성일: 2024-06-18
 * @수정일: 2024-06-19
 * @내용: 비밀번호 입력 전 유저 존재 유무 및 신규 비밀번호 유효성 검증
 */
@Service
public class MyInfoServiceImpl implements MyInfoService {

    private final MyInfoMapper myInfoMapper;

    @Autowired
    public MyInfoServiceImpl(MyInfoMapper myInfoMapper) {
        this.myInfoMapper = myInfoMapper;
    }

    /**
     * @제목: 비밀번호 수정 유효성 검증
     * @생성일: 2024-06-18
     * @수정일: 2024-06-19
     * @내용: 비밀번호 수정시 유저의 존재 유무, 소셜로그인 유저확인, 현재 사용중인 비밀번호
     * 인지 확인 검증
     */
    @Override
    public void updatePassword(Member member, MyInformation myInformation) {
        String newPassword = myInformation.getNewPassword();
        Member existMember = myInfoMapper.findByNo(member.getMemberNo());

        if (existMember == null) {
            throw new RuntimeException("해당 유저가 존재하지 않습니다.");
        }
        // 소셜로그인 회원 예외처리
        if (!StringUtils.hasText(member.getMemberId()) || !StringUtils.hasText(existMember.getMemberPw())) {
            throw new RuntimeException("소셜로그인 회원은 비밀번호 수정 불가합니다.");
        }
        if (!existMember.getMemberPw().equals(member.getMemberPw())) {
            throw new RuntimeException("현재 비밀번호가 다릅니다.");
        }
        if (newPassword.equals(member.getMemberPw())) {
            throw new RuntimeException("현재 사용중인 비밀번호와 같습니다.");
        }
        myInfoMapper.updatePassword(existMember.getMemberNo(), newPassword);
    }

    /**
     * @제목: 닉네임 수정 유효성 검증
     * @생성일: 2024-06-20
     * @수정일: 2024-07-02
     * @수정내용: 받는 데이터 타입 Map형태로 변경
     */
    @Override
    public void updateMemberNick(Member member, MyInformation myInformation) {
        String newMemberNick = myInformation.getNewMemberNick();

        Member existMember = myInfoMapper.findByNo(member.getMemberNo());

        if (existMember == null) {
            throw new RuntimeException("해당 유저가 존재하지 않습니다.");
        }
        if (newMemberNick.equals(existMember.getMemberNick())) {
            throw new RuntimeException("현재 닉네임과 동일한 닉네임");
        }

        Member nicknameOwner = myInfoMapper.findByNickname(newMemberNick);
        if (nicknameOwner != null) {
            throw new RuntimeException("사용중인 닉네임");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("memberNo", member.getMemberNo());
        params.put("newMemberNick", newMemberNick);

        myInfoMapper.updateMemberNick(params);
    }

    @Override
    public void updateMemberAddress(Member member, MyInformation myInformation) {
        String newMemberAddress = myInformation.getNewMemberAddress();
        Member existMember = myInfoMapper.findByNo(member.getMemberNo());

        if (existMember == null) {
            throw new RuntimeException("해당 유저가 존재하지 않습니다.");
        }
        myInfoMapper.updateMemberAddress(member.getMemberNo(), newMemberAddress);
    }

    // mapper에서 String 타입으로 받기로 해서 String 선언
    @Override
    public Member findByNo(String memberNo) {
        Member member = myInfoMapper.findByNo(memberNo);
        return member;
    }

    @Override
    public Member findByNickname(String memberNick) {
        Member member = myInfoMapper.findByNo(memberNick);
        return member;
    }

    @Override
    public Member findByAddress(String memberAddress) {
        Member member = myInfoMapper.findByAddress(memberAddress);
        return member;
    }

    @Override
    public Member findByPhone(String memberNo) {
        Member member = myInfoMapper.findByPhone(memberNo);
        return member;
    }

    @Override
    public void updateStatusCode(String memberNo, String newStatusCode) {
        Member existMember = myInfoMapper.findByNo(memberNo);

        if (existMember == null) {
            throw new RuntimeException("해당 유저가 존재하지 않습니다.");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("memberNo", memberNo);
        params.put("newStatusCode", newStatusCode);

        myInfoMapper.updateStatusCode(params);
    }

}
