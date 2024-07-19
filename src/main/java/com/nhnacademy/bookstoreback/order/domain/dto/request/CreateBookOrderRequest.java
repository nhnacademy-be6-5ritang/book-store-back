package com.nhnacademy.bookstoreback.order.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateBookOrderRequest(
	@NotNull Long bookId,
	Long orderId,
	@NotNull Integer quantity
) {
}
