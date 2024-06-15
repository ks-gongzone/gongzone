package com.gongzone.central.member.point.controller;

import com.gongzone.central.member.point.domain.Point;
import com.gongzone.central.member.point.service.PointServiceImpl;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class PointController {
	private final PointServiceImpl pointServiceImpl;


	public PointController(PointServiceImpl pointServiceImpl) {
		this.pointServiceImpl = pointServiceImpl;
	}


	@GetMapping("/{memberNo}/point")
	public Map<String, Point> getMemberPoint(@PathVariable String memberNo) {
		Map<String, Point> response = new HashMap<>();

		Point result = pointServiceImpl.get(memberNo);
		response.put("result", result);

		return response;
	}

}
