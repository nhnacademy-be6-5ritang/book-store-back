package com.nhnacademy.bookstoreback.order.domain.dto.response;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;

import lombok.Builder;

@Builder
public record CreateBookOrderResponse(
	Book book,
	CreateBookOrderGetOrderResponse getOrderResponse,
	BigDecimal quantity
) {
	public static CreateBookOrderResponse from(Book book, CreateBookOrderGetOrderResponse getOrderResponse,
		BigDecimal quantity) {
		return CreateBookOrderResponse.builder()
			.book(book)
			.getOrderResponse(getOrderResponse)
			.quantity(quantity)
			.build();
	}
}
