package com.gongzone.central.point.controller;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.login.security.JwtUtil;
import com.gongzone.central.member.login.service.MemberDetails;
import com.gongzone.central.point.domain.Point;
import com.gongzone.central.point.domain.PointChange;
import com.gongzone.central.point.domain.PointHistory;
import com.gongzone.central.point.service.PointService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
	private final JwtUtil jwtUtil;

	/**
	 * 회원의 포인트 사용 내역을 응답으로 반환한다.
	 *
	 * @param memberPointNo 회원 포인트 번호
	 * @return 포인트 사용내역
	 */
	@GetMapping("/{memberPointNo}/point/history")
	public Map<String, List<PointHistory>> getMemberPointHistory(@PathVariable String memberPointNo) {
		return pointService.getAllHistory(memberPointNo);
	}

	/**
	 * 회원이 현재 보유한 포인트를 응답으로 반환한다.
	 *
	 * @param memberPointNo 회원 포인트 번호
	 * @return 현재 보유 포인트
	 */
	@GetMapping("/{memberPointNo}/point")
	public Map<String, Integer> getMemberPoint(@PathVariable String memberPointNo) {
		return pointService.getCurrentPoint(memberPointNo);
	}

	// 포인트 예시
	@GetMapping("/point/test")
	public ResponseEntity<Point> getMemberPointTest(Authentication authentication) {
		MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();

		String token = memberDetails.getToken();

		String pointNo = jwtUtil.extractPointNo(token);

		Point point = pointService.getPoint(pointNo);

		return ResponseEntity.ok(point);
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
											   @RequestBody PointChange request) {
		Map<String, String> response = new HashMap<>();
		if (request.getChangeType() == null) {
			response.put("result", "FAILED_BAD_REQUEST");
			return response;
		}

		try {
			pointService.updateMemberPoint(memberPointNo, request);
			response.put("result", "SUCCESS");
		} catch (RuntimeException ignored) {
			response.put("result", "FAILED_INTERNAL_ERROR");
		}

		return response;
	}

	/**
	 * 회원의 포인트를 인출하고 결과를 반환한다.
	 *
	 * @param memberPointNo 회원 포인트 번호
	 * @param request       인출 정보가 담긴 요청
	 * @return 포인트 인출 결과
	 */
	@PostMapping("/{memberPointNo}/point/withdraw")
	public Map<String, String> postPointWithdraw(@PathVariable String memberPointNo,
												 @RequestBody PointChange request) {
		Map<String, String> response = new HashMap<>();
		if (request.getChangeType() == null) {
			response.put("result", "FAILED_BAD_REQUEST");
			return response;
		}

		try {
			pointService.updateMemberPoint(memberPointNo, request);
			response.put("result", "SUCCESS");
		} catch (RuntimeException ignored) {
			response.put("result", "FAILED_INTERNAL_ERROR");
		}

		return response;
	}

}
