package com.nhnacademy.bookstoreback.order.domain.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.nhnacademy.bookstoreback.order.domain.entity.BookOrder;

import lombok.Builder;

@Builder
public record UpdateBookOrderResponse(
	String orderInfoId,
	LocalDateTime orderDate,
	BigDecimal totalPrice,
	String bookTitle,
	BigDecimal bookPrice,
	Integer quantity
) {
	public static UpdateBookOrderResponse from(BookOrder bookOrder) {
		return UpdateBookOrderResponse.builder()
			.orderInfoId(bookOrder.getOrder().getOrderInfoId())
			.orderDate(bookOrder.getOrder().getOrderDate())
			.totalPrice(bookOrder.getOrder().getOrderPrice())
			.bookTitle(bookOrder.getBook().getBookTitle())
			.bookPrice(bookOrder.getBook().getBookPrice())
			.quantity(bookOrder.getBookQuantity())
			.build();
	}
}
