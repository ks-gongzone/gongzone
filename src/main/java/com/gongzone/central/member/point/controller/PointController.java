package com.gongzone.central.member.point.controller;

import com.gongzone.central.member.point.domain.PointCharge;
import com.gongzone.central.member.point.domain.PointHistory;
import com.gongzone.central.member.point.service.PointService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class PointController {
	private final PointService pointService;


	/**
	 * 회원의 포인트 사용 내역을 응답으로 반환한다.
	 *
	 * @param memberPointNo 회원 포인트 번호
	 * @return 포인트 사용내역
	 */
	@GetMapping("/{memberPointNo}/point/history")
	public Map<String, List<PointHistory>> getMemberPointHistory(@PathVariable String memberPointNo) {
		Map<String, List<PointHistory>> response = pointService.getAllHistory(memberPointNo);

		return response;
	}

	/**
	 * 회원이 현재 보유한 포인트를 응답으로 반환한다.
	 *
	 * @param memberPointNo 회원 포인트 번호
	 * @return 현재 보유 포인트
	 */
	@GetMapping("/{memberPointNo}/point")
	public Map<String, Integer> getMemberPoint(@PathVariable String memberPointNo) {
		Map<String, Integer> response = pointService.getCurrentPoint(memberPointNo);

		return response;
	}

	/**
	 * 회원의 포인트를 충전하고 결과를 반환한다.
	 *
	 * @param memberPointNo 회원 포인트 번호
	 * @param request       충전 정보가 담긴 요청
	 * @return 포인트 충전 결과
	 */
	@PostMapping("/{memberPointNo}/point/charge")
	public Map<String, String> postPointCharge(@PathVariable String memberPointNo,
											   @RequestBody PointCharge request) {
		Map<String, String> response = pointService.chargeMemberPoint(memberPointNo, request);

		return response;
	}

}
