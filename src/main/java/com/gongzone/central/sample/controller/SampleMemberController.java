package com.gongzone.central.sample.controller;

import com.gongzone.central.sample.service.SampleMemberService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller  // 컨트롤러 빈 설정
@ResponseBody  // 서버에서 렌더링된 페이지가 아닌, 결과 자체를 응답
@RequestMapping("/test")  // 메인 맵핑 경로
public class SampleMemberController {
	private final SampleMemberService sampleService;

	// 의존성 주입 - DI
	public SampleMemberController(SampleMemberService sampleService) {
		this.sampleService = sampleService;
	}


	// 샘플 코드입니다: 구현 코드에는 주석 전부 제거해주세요.
	@GetMapping("/member/{memberNo}")  // 메인 경로(/test) 내부 (/member/?) 경로로 들어오는 요청에 대해
	public Map<String, String> getSampleMember(@PathVariable("memberNo") String memberNo) {  // 요청 경로의 memberNo 부분을 변수로 사용
		Map<String, String> response = new HashMap<>();  // 응답을 담을 객체 생성

		String result = sampleService.findSampleMemberByMemberNo(memberNo);  // 서비스 레이어에 데이터 요청
		response.put("result", result);  // 응답 객체에 삽입

		return response;  // 생성된 결과를 응답
	}

}
