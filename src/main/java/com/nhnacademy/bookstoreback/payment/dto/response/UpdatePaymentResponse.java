package com.nhnacademy.bookstoreback.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdatePaymentResponse(
	String status
) {
	public static UpdatePaymentResponse from(String status) {
		return UpdatePaymentResponse.builder()
			.status(status)
			.build();
	}
}
