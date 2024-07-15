package com.gongzone.central.party.after.controller.admin;

import com.gongzone.central.common.Response.Result;
import com.gongzone.central.party.after.service.admin.AdminPartyAfterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/_admin/api/party")
public class AdminPartyAfterController {

	private final AdminPartyAfterService adminPartyAfterService;

	@Operation(summary = "(관리자) 파티를 정산한다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description = "SUCCESS",
						 content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500",
						 description = "FAILED_INTERNAL_ERROR"),
	})
	@PostMapping("/{partyNo}/settlement")
	public ResponseEntity<Result> adminPostPartySettlement(@PathVariable String partyNo) {
		ResponseEntity<Result> response;

		try {
			adminPartyAfterService.settlement(partyNo);
			response = ResponseEntity.ok().body(new Result("SUCCESS"));
		} catch (Exception e) {
			response = ResponseEntity.internalServerError().body(new Result("FAILED_INTERNAL_ERROR"));
			System.err.printf("Exception:\n\t%s\n", e);
			System.err.printf("\tCaused by: %s\n", e.getCause() != null ? e.getCause().toString() : "null");
		}

		return response;
	}

}
