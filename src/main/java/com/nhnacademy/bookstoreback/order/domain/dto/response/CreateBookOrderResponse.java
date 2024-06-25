package com.nhnacademy.bookstoreback.order.domain.dto.response;

import lombok.Builder;

@Builder
public record CreateBookOrderResponse(
	CreateBookOrderGetBookResponse getBookResponse,
	CreateBookOrderGetOrderResponse getOrderResponse,
	Integer quantity,
	Long orderListId
) {
	public static CreateBookOrderResponse from(CreateBookOrderGetBookResponse createBookOrderGetBookResponse,
		CreateBookOrderGetOrderResponse getOrderResponse,
		Integer quantity, Long orderListId) {
		return CreateBookOrderResponse.builder()
			.getBookResponse(createBookOrderGetBookResponse)
			.getOrderResponse(getOrderResponse)
			.quantity(quantity)
			.orderListId(orderListId)
			.build();
	}
}
