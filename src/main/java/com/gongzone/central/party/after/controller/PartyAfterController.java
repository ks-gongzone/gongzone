package com.gongzone.central.party.after.controller;

import com.gongzone.central.common.Response.Result;
import com.gongzone.central.party.after.domain.PartyPurchaseDetail;
import com.gongzone.central.party.after.domain.Reception;
import com.gongzone.central.party.after.domain.Shipping;
import com.gongzone.central.party.after.service.PartyAfterService;
import com.gongzone.central.point.domain.request.PointDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
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
	public ResponseEntity<Result> postPartyPurchase(@Parameter(description = "파티 고유번호(Pxxxxxx)")
													@PathVariable String partyNo,
													@Parameter(description = "회원 고유번호(Mxxxxxx)")
													@PathVariable String memberNo,
													@Parameter(description = "파티 결제 요청 객체")
													@RequestBody PointDTO<PartyPurchaseDetail> request) {
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

	@Operation(summary = "파티의 배송 정보를 입력한다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description = "SUCCESS",
						 content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500",
						 description = "FAILED_INTERNAL_ERROR"),
	})
	@PatchMapping("/{partyNo}/shipping/{shippingNo}")
	public ResponseEntity<Result> postPartyShipping(@Parameter(description = "파티 고유번호(Pxxxxxx)")
													@PathVariable String partyNo,
													@Parameter(description = "배송현황 고유번호")
													@PathVariable String shippingNo,
													@Parameter(description = "배송현황 객체")
													@RequestBody Shipping shipping) {
		ResponseEntity<Result> response;

		try {
			partyAfterService.updateShipping(partyNo, shippingNo, shipping);
			response = ResponseEntity.ok().body(new Result("SUCCESS"));
		} catch (Exception e) {
			System.err.printf("Exception during postPartyShipping: \n\t%s\n", e);
			System.err.printf("\tCaused by: %s\n", e.getCause() != null ? e.getCause().toString() : "null");
			response = ResponseEntity.internalServerError().body(new Result("FAILED_INTERNAL_ERROR"));
		}

		return response;
	}

	@Operation(summary = "파티의 배송을 완료한다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description = "SUCCESS",
						 content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500",
						 description = "FAILED_INTERNAL_ERROR"),
	})
	@PostMapping("/{partyNo}/shipping/{shippingNo}/complete")
	public ResponseEntity<Result> postPartyShippingComplete(@Parameter(description = "파티 고유번호(Mxxxxxx)")
															@PathVariable String partyNo,
															@Parameter(description = "배송현황 고유번호")
															@PathVariable String shippingNo) {
		ResponseEntity<Result> response;

		try {
			partyAfterService.updateShippingComplete(partyNo, shippingNo);
			response = ResponseEntity.ok().body(new Result("SUCCESS"));
		} catch (Exception e) {
			System.err.printf("Exception during postPartyShippingComplete: \n\t%s\n", e);
			System.err.printf("\tCaused by: %s\n", e.getCause() != null ? e.getCause().toString() : "null");
			response = ResponseEntity.internalServerError().body(new Result("FAILED_INTERNAL_ERROR"));
		}

		return response;
	}

	@Operation(summary = "파티원 수취 현황을 등록한다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description = "SUCCESS",
						 content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "500",
						 description = "FAILED_INTERNAL_ERROR"),
	})
	@PatchMapping("/{partyNo}/reception/{receptionNo}")
	public ResponseEntity<Result> patchPartyReception(@Parameter(description = "파티 고유번호(Pxxxxxx)")
													  @PathVariable String partyNo,
													  @Parameter(description = "수취현황 고유번호(Rxxxxxx)")
													  @PathVariable String receptionNo,
													  @Parameter(description = "수취현황 객체")
													  @RequestBody Reception reception) {
		ResponseEntity<Result> response;

		try {
			partyAfterService.updateReceptionComplete(partyNo, receptionNo, reception);
			response = ResponseEntity.ok().body(new Result("SUCCESS"));
		} catch (Exception e) {
			System.err.printf("Exception during : \n\t%s\n", e);
			System.err.printf("\tCaused by: %s\n", e.getCause() != null ? e.getCause().toString() : "null");
			response = ResponseEntity.internalServerError().body(new Result("FAILED_INTERNAL_ERROR"));
		}

		return response;
	}


	// Below for test
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
