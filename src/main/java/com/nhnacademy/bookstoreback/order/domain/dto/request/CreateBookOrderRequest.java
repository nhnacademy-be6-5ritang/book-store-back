package com.nhnacademy.bookstoreback.order.domain.dto.request;

import lombok.Builder;

@Builder
public record CreateBookOrderRequest(
	Long bookId,
	Long orderId,
	Integer quantity
) {
}
