package com.gongzone.central.sample.controller;

import com.gongzone.central.sample.service.SampleMemberService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@ResponseBody
@RequestMapping("/test")
public class SampleMemberController {
	private final SampleMemberService sampleService;

	public SampleMemberController(SampleMemberService sampleService) {
		this.sampleService = sampleService;
	}


	@GetMapping("/member/{memberNo}")
	public Map<String, String> getSampleMember(@PathVariable("memberNo") String memberNo) {
		Map<String, String> response = new HashMap<>();

		String result = sampleService.findSampleMemberByMemberNo(memberNo);
		response.put("result", result);

		return response;
	}

}
