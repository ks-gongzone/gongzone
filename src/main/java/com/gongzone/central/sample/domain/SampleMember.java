package com.gongzone.central.sample.domain;

import lombok.Data;


@Data  // Lombok 라이브러리 통해 getter/setter 등 생성하기 위해 사용
public class SampleMember {
	private final String memberNo;
	private final String memberId;

}
