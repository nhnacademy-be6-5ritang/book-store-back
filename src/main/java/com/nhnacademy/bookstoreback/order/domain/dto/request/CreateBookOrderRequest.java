package com.nhnacademy.bookstoreback.order.domain.dto.request;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record CreateBookOrderRequest(
	Long bookId,
	Long orderId,
	BigDecimal quantity
) {
}
