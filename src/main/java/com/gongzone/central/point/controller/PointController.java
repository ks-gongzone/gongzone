package com.gongzone.central.point.controller;

import com.gongzone.central.common.Response.Result;
import com.gongzone.central.point.domain.PointHistory;
import com.gongzone.central.point.domain.request.PointChargeRequest;
import com.gongzone.central.point.domain.request.PointWithdrawRequest;
import com.gongzone.central.point.service.PointService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
	 * GET - 포인트 사용내역 list
	 *
	 * @param memberPointNo 회원 포인트 번호
	 * @return 포인트 사용내역
	 */
	@GetMapping("/{memberPointNo}/point/history")
	public ResponseEntity<Result> getAllMemberPointHistory(@PathVariable String memberPointNo) {
		ResponseEntity<Result> response;

		try {
			List<PointHistory> pointHistories = pointService.getAllHistory(memberPointNo);
			response = ResponseEntity.ok(new Result(pointHistories));
		} catch (Exception e) {
			System.err.println("Exception during getAllMemberPointHistory: " + e.getClass().getName());
			System.err.println(e.getCause().toString());
			response = ResponseEntity.internalServerError().body(new Result("FAILED_INTERNAL_ERROR"));
		}

		return response;
	}

	/**
	 * GET - 포인트 사용내역
	 *
	 * @param pointHistoryNo 포인트 내역 번호
	 * @return
	 */
	@GetMapping("/{memberPointNo}/point/history/{pointHistoryNo}")
	public ResponseEntity<Result> getMemberPointHistory(@PathVariable String memberPointNo,
														@PathVariable String pointHistoryNo) {
		ResponseEntity<Result> response;

		try {
			PointHistory pointHistory = pointService.getHistory(memberPointNo, pointHistoryNo);
			response = ResponseEntity.ok(new Result(pointHistory));
		} catch (Exception e) {
			System.err.println("Exception during getMemberPointHistory: " + e.getClass().getName());
			System.err.println(e.getCause().toString());
			response = ResponseEntity.internalServerError().body(new Result("FAILED_INTERNAL_ERROR"));
		}

		return response;
	}

	/**
	 * GET - 현재 보유 포인트
	 *
	 * @param memberPointNo 회원 포인트 번호
	 * @return 현재 보유 포인트
	 */
	@GetMapping("/{memberPointNo}/point")
	public ResponseEntity<Result> getMemberPoint(@PathVariable String memberPointNo) {
		ResponseEntity<Result> response;

		try {
			Integer point = pointService.getCurrentPoint(memberPointNo);
			response = ResponseEntity.ok(new Result(point));
		} catch (Exception e) {
			System.err.println("Exception during getMemberPoint: " + e.getClass().getName());
			System.err.println(e.getCause().toString());
			response = ResponseEntity.internalServerError().body(new Result("FAILED_INTERNAL_ERROR"));
		}

		return response;
	}

	/**
	 * POST - 포인트 충전 요청 처리
	 *
	 * @param memberPointNo 회원 포인트 번호
	 * @param request       충전 정보가 담긴 요청
	 * @return 포인트 충전 결과
	 */
	@PostMapping("/{memberPointNo}/point/charge")
	public ResponseEntity<Result> postMemberPointCharge(@PathVariable String memberPointNo,
														@RequestBody PointChargeRequest request) {
		ResponseEntity<Result> response;

		try {
			pointService.charge(memberPointNo, request);
			response = ResponseEntity.ok(new Result("SUCCESS"));
		} catch (RuntimeException e) {
			System.err.println("Error during postMemberPointCharge: " + e.getClass().getName());
			System.err.println(e.getCause().toString());
			response = ResponseEntity.internalServerError().body(new Result("FAILED_INTERNAL_ERROR"));
		}

		return response;
	}

	/**
	 * POST - 포인트 인출 요청에 대한 처리
	 *
	 * @param memberPointNo 회원 포인트 번호
	 * @param request       인출 정보가 담긴 요청
	 * @return 포인트 인출 결과
	 */
	@PostMapping("/{memberPointNo}/point/withdraw")
	public ResponseEntity<Result> postMemberPointWithdraw(@PathVariable String memberPointNo,
														  @RequestBody PointWithdrawRequest request) {
		ResponseEntity<Result> response;

		try {
			pointService.withdraw(memberPointNo, request);
			response = ResponseEntity.ok(new Result("SUCCESS"));
		} catch (RuntimeException e) {
			System.err.println("Error during point postMemberPointWithdraw: " + e.getClass().getName());
			System.err.println(e.getCause().toString());
			response = ResponseEntity.internalServerError().body(new Result("FAILED_INTERNAL_ERROR"));
		}

		return response;
	}

}
