package com.gongzone.central.member.login.service;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.domain.MemberLevel;
import com.gongzone.central.member.login.mapper.LoginMapper;
import com.gongzone.central.point.domain.Point;
import com.gongzone.central.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

	private final PointService pointService;

	private final LoginMapper loginMapper;


	@Override
	public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
		Member member;
		if (identifier.contains("@")) {
			member = loginMapper.getMemberByEmail(identifier);
		} else if (identifier.startsWith("M")) {
			member = loginMapper.getMemberByNo(identifier);
		} else {
			member = loginMapper.getMemberById(identifier);
		}

		if (member == null) {
			throw new UsernameNotFoundException("User not found");
		}
		int memberLevelValue = member.getMemberLevel();
		if (memberLevelValue == 0) {
			throw new IllegalArgumentException("Invalid member level: " + memberLevelValue);
		}

		MemberLevel level = MemberLevel.fromLevel(memberLevelValue);

		Point pointNo = pointService.getPoint(member.getMemberNo());
		if (pointNo == null) {
			throw new IllegalArgumentException("Point information not found for memberNo: " + member.getMemberNo());
		}

		return new MemberDetails(member, pointNo);
	}

}
