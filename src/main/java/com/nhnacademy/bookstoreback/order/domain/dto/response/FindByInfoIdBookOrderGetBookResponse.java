package com.nhnacademy.bookstoreback.order.domain.dto.response;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;

import lombok.Builder;

@Builder
public record FindByInfoIdBookOrderGetBookResponse(
	String bookTitle,
	BigDecimal bookPrice,
	String bookDescription
) {
	public static FindByInfoIdBookOrderGetBookResponse from(Book book) {
		return FindByInfoIdBookOrderGetBookResponse.builder()
			.bookTitle(book.getBookTitle())
			.bookPrice(book.getBookPrice())
			.bookDescription(book.getBookDescription())
			.build();
	}
}
