package com.gongzone.central.point.controller;

import com.gongzone.central.common.Response.Result;
import com.gongzone.central.point.domain.PointHistory;
import com.gongzone.central.point.domain.request.PointChargeRequest;
import com.gongzone.central.point.domain.request.PointWithdrawRequest;
import com.gongzone.central.point.service.PointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class PointController {
	private final PointService pointService;

	@Operation(summary = "특정 회원의 포인트 내역 전체를 요청한다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description = "포인트 사용 내역 객체",
						 content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500",
						 description = "FAILED_INTERNAL_ERROR"),
	})
	@GetMapping("/{memberPointNo}/point/history")
	public ResponseEntity<Result> getMemberPointHistories(@Parameter(description = "회원 포인트 번호(MPxxxxxx)")
														  @PathVariable String memberPointNo,
														  @Parameter(description = "페이지 크기(int)")
														  @RequestParam(defaultValue = "10") int pageSize,
														  @Parameter(description = "요청 페이지(int)")
														  @RequestParam(defaultValue = "1") int pageNo) {
		ResponseEntity<Result> response;

		try {
			List<PointHistory> pointHistories = pointService.getHistories(memberPointNo, pageSize, pageNo);
			response = ResponseEntity.ok(new Result(pointHistories));
		} catch (Exception e) {
			System.err.println("Exception during getAllMemberPointHistory: " + e.getClass().getName());
			System.err.println(e.getCause().toString());
			response = ResponseEntity.internalServerError().body(new Result("FAILED_INTERNAL_ERROR"));
		}

		return response;
	}

	@Operation(summary = "특정 회원의 특정 포인트 내역을 요청한다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description = "포인트 사용 내역 객체",
						 content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500",
						 description = "FAILED_INTERNAL_ERROR"),
	})
	@GetMapping("/{memberPointNo}/point/history/{pointHistoryNo}")
	public ResponseEntity<Result> getMemberPointHistory(@Parameter(description = "회원 포인트 번호(MPxxxxxx)")
														@PathVariable String memberPointNo,
														@Parameter(description = "포인트 내역 번호(PHxxxxxx)")
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

	@Operation(summary = "현재 포인트 보유량을 요청한다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description = "현재 보유 포인트",
						 content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500",
						 description = "FAILED_INTERNAL_ERROR"),
	})
	@GetMapping("/{memberPointNo}/point")
	public ResponseEntity<Result> getMemberPoint(@Parameter(description = "회원 포인트 번호(MPxxxxxx)")
												 @PathVariable String memberPointNo) {
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

	@Operation(summary = "포인트 충전을 요청한다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description = "SUCCESS",
						 content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500",
						 description = "FAILED_INTERNAL_ERROR"),
	})
	@PostMapping("/{memberPointNo}/point/charge")
	public ResponseEntity<Result> postMemberPointCharge(@Parameter(description = "회원 포인트 번호(MPxxxxxx)")
														@PathVariable String memberPointNo,
														@Parameter(description = "포인트 충전 요청 객체(json)")
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

	@Operation(summary = "포인트 인출을 요청한다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description = "SUCCESS",
						 content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500",
						 description = "FAILED_INTERNAL_ERROR"),
	})
	@PostMapping("/{memberPointNo}/point/withdraw")
	public ResponseEntity<Result> postMemberPointWithdraw(@Parameter(description = "회원 포인트 번호(MPxxxxxx)")
														  @PathVariable String memberPointNo,
														  @Parameter(description = "포인트 인출 요청 객체(json)")
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
