package com.gongzone.central.member.login.service;

import com.gongzone.central.member.domain.Member;

import com.gongzone.central.member.domain.MemberLevel;
import com.gongzone.central.member.login.mapper.LoginMapper;
import com.gongzone.central.member.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberDetailsService implements UserDetailsService {

    @Autowired
    private LoginMapper loginMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = loginMapper.getMemberFromId(username);
        if (member == null) {
            throw new UsernameNotFoundException("User not found");
        }

        System.out.println("Loaded member: " + member);
        System.out.println("Member level: " + member.getMemberLevel());

        int memberLevelValue = member.getMemberLevel();
        if (memberLevelValue == 0) {
            throw new IllegalArgumentException("Invalid member level: " + memberLevelValue);
        }

        MemberLevel level = MemberLevel.fromLevel(memberLevelValue);

        // 빌더 패턴 사용하여 Member 객체 재구성
        member = Member.builder()
                .memberNo(member.getMemberNo())
                .memberLevel(level.getLevel())
                .memberId(member.getMemberId())
                .memberPw(member.getMemberPw())
                .memberName(member.getMemberName())
                .memberEmail(member.getMemberEmail())
                .memberPhone(member.getMemberPhone())
                .memberGender(member.getMemberGender())
                .memberAddress(member.getMemberAddress())
                .memberBirthday(member.getMemberBirthday())
                .memberNick(member.getMemberNick())
                .memberStatus(member.getMemberStatus())
                .build();

        return new MemberDetails(member);
    }
}