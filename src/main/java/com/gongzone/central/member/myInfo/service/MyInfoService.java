package com.gongzone.central.member.myInfo.service;

import com.gongzone.central.member.myInfo.domain.MyInformation;
import com.gongzone.central.member.domain.Member;

/**
 * @제목: 내정보 서비스 인터페이스 구현
 * @생성일: 2024-06-18
 * @수정일: 2024-06-26
 * @내용: Member 클래스에 선언된 변수 사용
 */
public interface MyInfoService {
	Member findByNo(String memberNo); // memberNo로 회원 정보를 가져오는 메서드
	Member findByNickname(String memberNick);
	Member findByAddress(String memberAddress);
	Member findByPhone(String memberNo);

	void updatePassword (Member member, MyInformation myInformation);
	void updateMemberNick (Member member, MyInformation myInformation);
	void updateMemberAddress(Member member, MyInformation myInformation);
}
