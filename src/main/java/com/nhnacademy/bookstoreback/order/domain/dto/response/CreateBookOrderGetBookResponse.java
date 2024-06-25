package com.nhnacademy.bookstoreback.order.domain.dto.response;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;

import lombok.Builder;

@Builder
public record CreateBookOrderGetBookResponse(
	String bookTitle,
	BigDecimal bookPrice,
	String bookDescription
) {
	public static CreateBookOrderGetBookResponse from(Book book) {
		return CreateBookOrderGetBookResponse.builder()
			.bookTitle(book.getBookTitle())
			.bookPrice(book.getBookPrice())
			.bookDescription(book.getBookDescription())
			.build();
	}
}
