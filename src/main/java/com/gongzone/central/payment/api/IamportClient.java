package com.gongzone.central.payment.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gongzone.central.payment.domain.detail.PaymentInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class IamportClient {
	private static final String BASE_URL = "https://api.portone.io/";
	private final String secretKey;
	private final String auth;
	private final WebClient webClient;
	private final ObjectMapper objectMapper;


	public IamportClient(String secretKey, WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
		this.secretKey = secretKey;
		this.auth = "PortOne " + secretKey;
		this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
		this.objectMapper = objectMapper;
	}

	public Mono<PaymentInfo> getPaymentInfo(String paymentId) {
		return getRequest("/payments/" + paymentId);
	}


	private Mono<PaymentInfo> getRequest(String url) {
		return this.webClient.get()
							 .uri(url)
							 .header("Authorization", auth)
							 .retrieve()
							 .bodyToMono(String.class)
							 .flatMap(json -> {
								 try {
									 // 전체 JSON 문자열을 파싱하여 JsonNode 객체로 변환
									 JsonNode rootNode = objectMapper.readTree(json);

									 // pgResponse 필드의 값을 추출하고 이스케이프 문자를 제거
									 String pgResponseString = rootNode.get("pgResponse")
																	   .asText()
																	   .replace("\\\"", "\"");

									 // pgResponseString을 PgResponse 객체로 변환
									 JsonNode pgResponseNode = objectMapper.readTree(pgResponseString);
									 ((ObjectNode) rootNode).set("pgResponse", pgResponseNode);

									 // PaymentDetail 객체로 변환
									 PaymentInfo paymentInfo = objectMapper.treeToValue(rootNode, PaymentInfo.class);

									 return Mono.just(paymentInfo);
								 } catch (Exception e) {
									 e.printStackTrace();
									 return Mono.error(new RuntimeException("Failed to parse JSON: " + json, e));
								 }
							 });
	}

}
