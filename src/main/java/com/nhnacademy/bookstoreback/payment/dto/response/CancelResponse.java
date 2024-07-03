package com.nhnacademy.bookstoreback.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record CancelResponse(
	String paymentKey,
	Long paymentId
) {
	public static CancelResponse from(String paymentKey, Long paymentId) {
		return CancelResponse.builder()
			.paymentKey(paymentKey)
			.paymentId(paymentId)
			.build();
	}
}
