package com.gongzone.central.sample.mapper;

import com.gongzone.central.sample.domain.SampleMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SampleMemberMapper {
	SampleMember findSampleMemberByMemberNo(@Param("memberNo") String memberNo);

}
