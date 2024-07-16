package com.gongzone.central.point.withdraw.controller;

import com.gongzone.central.common.Response.Result;
import com.gongzone.central.point.withdraw.service.WithdrawService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class WithdrawController {

	private final WithdrawService withdrawService;


	@Operation(summary = "특정 포인트 인출 현황을 요청한다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description = "SUCCESS",
						 content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500",
						 description = "FAILED_INTERNAL_ERROR"),
	})
	@GetMapping("/{memberNo}/point/withdraw/{withdrawNo}")
	public ResponseEntity<Result> getPointWithdraw(@PathVariable String memberNo,
												   @PathVariable String withdrawNo) {
		ResponseEntity<Result> response;

		try {
			response = ResponseEntity.ok().body(new Result(withdrawService.get(withdrawNo)
			));
		} catch (Exception e) {
			System.err.printf("Exception during getPointWithdrawal: \n\t%s\n", e);
			System.err.printf("\tCaused by: %s\n", e.getCause() != null ? e.getCause().toString() : "null");
			response = ResponseEntity.internalServerError().body(new Result("FAILED_INTERNAL_ERROR"));
		}

		return response;
	}

	@Operation(summary = "포인트 인출 현황을 요청한다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description = "SUCCESS",
						 content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500",
						 description = "FAILED_INTERNAL_ERROR"),
	})
	@GetMapping("/{memberNo}/point/withdraw")
	public ResponseEntity<Result> getPointWithdraw(@PathVariable String memberNo,
												   @RequestParam(defaultValue = "10") int pageSize,
												   @RequestParam(defaultValue = "1") int page) {
		ResponseEntity<Result> response;

		try {
			response = ResponseEntity.ok().body(new Result(withdrawService.getMany(memberNo, pageSize, page)));
		} catch (Exception e) {
			System.err.printf("Exception during getPointWithdrawal: \n\t%s\n", e);
			System.err.printf("\tCaused by: %s\n", e.getCause() != null ? e.getCause().toString() : "null");
			response = ResponseEntity.internalServerError().body(new Result("FAILED_INTERNAL_ERROR"));
		}

		return response;
	}

}
