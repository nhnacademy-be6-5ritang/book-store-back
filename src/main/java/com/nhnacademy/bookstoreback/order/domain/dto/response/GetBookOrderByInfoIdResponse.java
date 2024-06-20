package com.nhnacademy.bookstoreback.order.domain.dto.response;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;

import lombok.Builder;

@Builder
public record GetBookOrderByInfoIdResponse(
	Book book,
	CreateBookOrderGetOrderResponse getOrderResponse,
	BigDecimal quantity
) {
	public static GetBookOrderByInfoIdResponse from(Book book, CreateBookOrderGetOrderResponse getOrderResponse,
		BigDecimal quantity) {
		return GetBookOrderByInfoIdResponse.builder()
			.book(book)
			.getOrderResponse(getOrderResponse)
			.quantity(quantity)
			.build();
	}
}
