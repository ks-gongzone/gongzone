package com.gongzone.central.point.payment.controller;

import com.gongzone.central.common.Response.Result;
import com.gongzone.central.point.domain.request.PointDTO;
import com.gongzone.central.point.payment.domain.Payment;
import com.gongzone.central.point.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class PaymentController {

	private final PaymentService paymentService;


	@Operation(summary = "포인트 충전을 요청한다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description = "SUCCESS",
						 content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500",
						 description = "FAILED_INTERNAL_ERROR"),
	})
	@PostMapping("/{memberNo}/point/payment")
	public ResponseEntity<Result> postMemberPointCharge(@Parameter(description = "회원 번호(Mxxxxxx)")
														@PathVariable String memberNo,
														@Parameter(description = "포인트 충전 요청 객체(json)")
														@RequestBody PointDTO<Payment> request) {
		ResponseEntity<Result> response;

		try {
			paymentService.charge(memberNo, request);
			response = ResponseEntity.ok(new Result("SUCCESS"));
		} catch (RuntimeException e) {
			System.err.printf("Exception:\n\t%s\n", e);
			System.err.printf("\tCaused by: %s\n", e.getCause() != null ? e.getCause().toString() : "null");
			response = ResponseEntity.internalServerError().body(new Result("FAILED_INTERNAL_ERROR"));
		}

		return response;
	}

	@Operation(summary = "특정 결제 정보를 요청한다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description = "SUCCESS",
						 content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500",
						 description = "FAILED_INTERNAL_ERROR"),
	})
	@GetMapping("/{memberNo}/point/payment/{paymentNo}")
	public ResponseEntity<Result> getPointPayment(@PathVariable String memberNo,
												  @PathVariable String paymentNo) {
		ResponseEntity<Result> response;

		try {
			response = ResponseEntity.ok().body(new Result(paymentService.get(paymentNo)));
		} catch (Exception e) {
			System.err.printf("Exception during getPointPayment: \n\t%s\n", e);
			System.err.printf("\tCaused by: %s\n", e.getCause() != null ? e.getCause().toString() : "null");
			response = ResponseEntity.internalServerError().body(new Result("FAILED_INTERNAL_ERROR"));
		}

		return response;
	}

	@Operation(summary = "특정 결제 현황의 상세 정보를 요청한다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description = "SUCCESS",
						 content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500",
						 description = "FAILED_INTERNAL_ERROR"),
	})
	@GetMapping("/{memberNo}/point/payment/{paymentNo}/detail")
	public ResponseEntity<Result> getPointPaymentDetail(@PathVariable String memberNo,
														@PathVariable String paymentNo) {
		ResponseEntity<Result> response;

		try {
			response = ResponseEntity.ok().body(new Result(paymentService.getDetail(paymentNo)));
		} catch (Exception e) {
			System.err.printf("Exception during getPointPaymentDetail: \n\t%s\n", e);
			System.err.printf("\tCaused by: %s\n", e.getCause() != null ? e.getCause().toString() : "null");
			response = ResponseEntity.internalServerError().body(new Result("FAILED_INTERNAL_ERROR"));
		}

		return response;
	}

	@Operation(summary = "회원의 결제 정보를 요청한다. ")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description = "SUCCESS",
						 content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500",
						 description = "FAILED_INTERNAL_ERROR"),
	})
	@GetMapping("/{memberNo}/point/payment")
	public ResponseEntity<Result> getPointPayments(@PathVariable String memberNo,
												   @RequestParam(defaultValue = "10") int pageSize,
												   @RequestParam(defaultValue = "1") int page) {
		ResponseEntity<Result> response;

		try {
			response = ResponseEntity.ok().body(new Result(paymentService.getMany(memberNo, pageSize, page)));
		} catch (Exception e) {
			System.err.printf("Exception during getPointPayments: \n\t%s\n", e);
			System.err.printf("\tCaused by: %s\n", e.getCause() != null ? e.getCause().toString() : "null");
			response = ResponseEntity.internalServerError().body(new Result("FAILED_INTERNAL_ERROR"));
		}

		return response;
	}

}
