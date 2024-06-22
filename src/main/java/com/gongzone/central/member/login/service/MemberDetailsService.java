package com.gongzone.central.member.login.service;

import com.gongzone.central.member.domain.Member;

import com.gongzone.central.member.domain.MemberLevel;
import com.gongzone.central.member.login.mapper.LoginMapper;
import com.gongzone.central.point.domain.Point;
import com.gongzone.central.point.mapper.PointMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Transient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final LoginMapper loginMapper;
    private final PointMapper pointMapper;

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

        System.out.println("memberLevelValue " + memberLevelValue);
        MemberLevel level = MemberLevel.fromLevel(memberLevelValue);

        Point pointNo = pointMapper.getPointNoByMemberNo(member.getMemberNo());
        System.out.println("포인트" + pointNo);
        if (pointNo == null) {
            throw new IllegalArgumentException("Point information not found for memberNo: " + member.getMemberNo());
        }

        return new MemberDetails(member, pointNo);
    }
}