package com.gongzone.central.party.after.controller;

import com.gongzone.central.common.Response.Result;
import com.gongzone.central.party.after.domain.request.PartyPurchaseRequest;
import com.gongzone.central.party.after.service.PartyAfterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
	@PostMapping("/purchase")
	public ResponseEntity<Result> postPartyPurchase(@RequestBody PartyPurchaseRequest partyPurchaseRequest) {
		ResponseEntity<Result> response;

		try {
			partyAfterService.purchase(partyPurchaseRequest);
			response = ResponseEntity.ok().body(new Result("SUCCESS"));
		} catch (Exception e) {
			response = ResponseEntity.internalServerError().body(new Result("FAILED_INTERNAL_ERROR"));
			System.err.println("Exception during postPartyPurchase: " + e.getClass().getName());
			System.err.println(e.getCause().toString());
		}

		return response;
	}

}
