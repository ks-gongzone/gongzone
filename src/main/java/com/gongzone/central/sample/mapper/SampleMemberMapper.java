package com.gongzone.central.sample.mapper;

import com.gongzone.central.sample.domain.SampleMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper  // 매퍼 인터페이스 빈
public interface SampleMemberMapper {
	SampleMember findSampleMemberByMemberNo(@Param("memberNo") String memberNo); // @Param(~) -> 파라미터 하나일 땐 생략 가능, 두 개 이상 시 무조건 사용

	// 매퍼 인터페이스의 구현체는 프레임워크에서 매퍼 xml파일을 근거로 대리 객체 생성
}
