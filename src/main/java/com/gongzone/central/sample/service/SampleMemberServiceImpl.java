package com.gongzone.central.sample.service;

import com.gongzone.central.sample.domain.SampleMember;
import com.gongzone.central.sample.mapper.SampleMemberMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SampleMemberServiceImpl implements SampleMemberService {
	private final SampleMemberMapper sampleMemberMapper;

	public SampleMemberServiceImpl(SampleMemberMapper sampleMemberMapper) {
		this.sampleMemberMapper = sampleMemberMapper;
	}


	@Override
	public String findSampleMemberByMemberNo(String memberNo) {
		SampleMember result = sampleMemberMapper.findSampleMemberByMemberNo(memberNo);
		System.out.println("result: " + result);

		if (result == null) {
			return "해당하는 회원이 없습니다.";
		} else {
			return "회원 id = " + result.getMemberId();
		}
	}

}
