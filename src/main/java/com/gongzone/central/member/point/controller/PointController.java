package com.gongzone.central.member.point.controller;

import com.gongzone.central.member.point.domain.Point;
import com.gongzone.central.member.point.service.PointService;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class PointController {
	private final PointService pointService;


	public PointController(PointService pointService) {
		this.pointService = pointService;
	}


	@GetMapping("/{memberNo}/point")
	public Map<String, Point> getMemberPoint(@PathVariable String memberNo) {
		Map<String, Point> response = pointService.get(memberNo);

		return response;
	}

}
