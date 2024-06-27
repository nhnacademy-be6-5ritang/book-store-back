package com.nhnacademy.bookstoreback.order.domain.dto.response;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;

import lombok.Builder;

@Builder
public record GetBookResponse(
	String bookTitle,
	String bookDeDescription,
	BigDecimal bookPrice,
	Boolean bookPackaging
) {
	public static GetBookResponse from(Book book) {
		return GetBookResponse.builder()
			.bookTitle(book.getBookTitle())
			.bookDeDescription(book.getBookDescription())
			.bookPrice(book.getBookPrice())
			.bookPackaging(book.isBookPackaging())
			.build();
	}
}
