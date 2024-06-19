package com.gongzone.central.member.login.service;

import com.gongzone.central.member.domain.Member;

import com.gongzone.central.member.domain.MemberLevel;
import com.gongzone.central.member.login.mapper.LoginMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class MemberDetailsService implements UserDetailsService {

    @Autowired
    private LoginMapper loginMapper;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Member member = loginMapper.getMemberFromId(loginId);
        if (member == null) {
            throw new UsernameNotFoundException("User not found");
        }

        int memberLevelValue = member.getMemberLevel();
        if (memberLevelValue == 0) {
            throw new IllegalArgumentException("Invalid member level: " + memberLevelValue);
        }

        MemberLevel level = MemberLevel.fromLevel(memberLevelValue);

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