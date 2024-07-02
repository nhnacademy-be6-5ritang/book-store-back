package com.nhnacademy.bookstoreback.payment.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record PaymentResponse(
	String paymentKey,
	String orderId,
	BigDecimal amount,
	String status,
	LocalDateTime date
) {
	public static PaymentResponse from(String paymentKey, String orderId, BigDecimal amount, String status,
		LocalDateTime date) {
		return PaymentResponse.builder()
			.paymentKey(paymentKey)
			.orderId(orderId)
			.amount(amount)
			.status(status)
			.date(date)
			.build();
	}
}
