package com.gongzone.central.sample.service;

import com.gongzone.central.sample.domain.SampleMember;
import com.gongzone.central.sample.mapper.SampleMemberMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service  // 서비스 레이어 빈
@Transactional  // 트랜잭션 수행, 메서드 실행 중 예외 발생 시 롤백, 정상 완료 시 커밋
public class SampleMemberServiceImpl implements SampleMemberService {
	private final SampleMemberMapper sampleMemberMapper;

	// 의존성 주입 - DI
	public SampleMemberServiceImpl(SampleMemberMapper sampleMemberMapper) {
		this.sampleMemberMapper = sampleMemberMapper;
	}


	@Override
	public String findSampleMemberByMemberNo(String memberNo) {
		SampleMember result = sampleMemberMapper.findSampleMemberByMemberNo(memberNo);  // 매퍼에 결과 요청
		System.out.println("result: " + result);

		if (result == null) {  // 결과가 없다면 매퍼가 null을 반환한다.
			return "해당하는 회원이 없습니다.";
		} else {
			return "회원 id = " + result.getMemberId();
		}
	}

}
