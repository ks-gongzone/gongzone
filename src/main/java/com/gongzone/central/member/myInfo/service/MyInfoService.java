package com.gongzone.central.member.myInfo.service;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.myInfo.domain.MyInformation;

public interface MyInfoService {

	void updatePassword(Member member, MyInformation myInformation);

	// mapper에서 String 타입으로 받기로 해서 String 선언
	Member findByNo(String memberNo);

}
