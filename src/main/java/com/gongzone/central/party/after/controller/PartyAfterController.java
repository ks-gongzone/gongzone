package com.gongzone.central.party.after.controller;

import com.gongzone.central.common.Response.Result;
import com.gongzone.central.party.after.domain.PartyPurchaseDetail;
import com.gongzone.central.party.after.service.PartyAfterService;
import com.gongzone.central.point.domain.request.PointRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/party")
public class PartyAfterController {

	private final PartyAfterService partyAfterService;


	@Operation(summary = "파티 참여자의 결제 정보를 업데이트한다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description = "SUCCESS",
						 content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500",
						 description = "FAILED_INTERNAL_ERROR"),
	})
	@PostMapping("/{partyNo}/purchase/{memberNo}")
	public ResponseEntity<Result> postPartyPurchase(@PathVariable String partyNo,
													@PathVariable String memberNo,
													@RequestBody PointRequest<PartyPurchaseDetail> request) {
		ResponseEntity<Result> response;

		try {
			partyAfterService.purchase(partyNo, memberNo, request);
			response = ResponseEntity.ok().body(new Result("SUCCESS"));
		} catch (Exception e) {
			response = ResponseEntity.internalServerError().body(new Result("FAILED_INTERNAL_ERROR"));
			System.err.printf("Exception:\n\t%s\n", e);
			System.err.printf("\tCaused by: %s\n", e.getCause() != null ? e.getCause().toString() : "null");
		}

		return response;
	}

	@Operation(summary = "테스트: 활성 파티를 삽입한다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description = "SUCCESS",
						 content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500",
						 description = "FAILED_INTERNAL_ERROR"),
	})
	@PostMapping("/test")
	public ResponseEntity<Result> postTestParty() {
		ResponseEntity<Result> response;

		try {
			partyAfterService.testInsertParty();
			response = ResponseEntity.ok().body(new Result("SUCCESS"));
		} catch (Exception e) {
			System.err.printf("Exception during postTestParty: \n\t%s\n", e);
			System.err.printf("\tCaused by: %s\n", e.getCause() != null ? e.getCause().toString() : "null");
			response = ResponseEntity.internalServerError().body(new Result("FAILED_INTERNAL_ERROR"));
		}

		return response;
	}

}
