package com.nhnacademy.bookstoreback.order.domain.dto.response;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;

import lombok.Builder;

@Builder
public record GetBookOrderGetBookResponse(
	String bookTitle,
	BigDecimal bookPrice,
	String bookDescription,
	boolean bookPackaging
) {
	public static GetBookOrderGetBookResponse from(Book book) {
		return GetBookOrderGetBookResponse.builder()
			.bookTitle(book.getBookTitle())
			.bookPrice(book.getBookPrice())
			.bookDescription(book.getBookDescription())
			.bookPackaging(book.isBookPackaging())
			.build();
	}
}
