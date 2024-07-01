package com.nhnacademy.bookstoreback.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nhnacademy.bookstoreback.payment.dto.entitiy.Payment;

import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record PaymentSaveResponse(
	String paymentKey
) {
	public static PaymentSaveResponse from(Payment payment) {
		return PaymentSaveResponse.builder()
			.paymentKey(payment.getPaymentKey())
			.build();
	}
}
