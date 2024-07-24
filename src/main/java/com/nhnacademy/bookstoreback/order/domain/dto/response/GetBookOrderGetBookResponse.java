package com.nhnacademy.bookstoreback.order.domain.dto.response;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;

import lombok.Builder;

@Builder
public record GetBookOrderGetBookResponse(
	String image,
	String bookTitle,
	BigDecimal bookPrice,
	String bookDescription,
	Long bookId
) {
	public static GetBookOrderGetBookResponse from(Book book) {
		String imageUrl = book.getBookImages().stream()
			.map(bookImage -> bookImage.getImage().getImageUrl())
			.findFirst()
			.orElse(null);

		return GetBookOrderGetBookResponse.builder()
			.bookTitle(book.getBookTitle())
			.bookPrice(book.getBookSalePrice())
			.bookDescription(book.getBookDescription())
			.bookId(book.getBookId())
			.image(imageUrl)
			.build();
	}
}
