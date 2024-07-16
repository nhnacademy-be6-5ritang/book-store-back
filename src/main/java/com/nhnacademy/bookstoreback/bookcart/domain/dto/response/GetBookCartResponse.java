package com.nhnacademy.bookstoreback.bookcart.domain.dto.response;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;

import lombok.Builder;

@Builder
public record GetBookCartResponse(
	Long bookId,
	String cartId,
	String bookImageUrl,
	String bookTitle,
	String authorName,
	String publisherName,
	BigDecimal bookPrice,
	BigDecimal bookSalePrice,
	BigDecimal bookSalePercent,
	int inventorQuantity,
	Integer bookQuantity
) {
	public static GetBookCartResponse fromEntity(Book book, Integer bookQuantity, String cartId) {
		String imageUrl = book.getBookImages().stream()
			.map(bookImage -> bookImage.getImage().getImageUrl())
			.findFirst()
			.orElse(null); // 이미지가 없는 경우 null 반환

		return GetBookCartResponse.builder()
			.bookId(book.getBookId())
			.cartId(cartId)
			.bookImageUrl(imageUrl)
			.bookTitle(book.getBookTitle())
			.authorName(book.getAuthor().getAuthorName())
			.publisherName(book.getPublisher().getPublisherName())
			.bookPrice(book.getBookPrice())
			.bookSalePrice(book.getBookSalePrice())
			.bookSalePercent(book.getBookSalePercent())
			.inventorQuantity(book.getBookQuantity())
			.bookQuantity(bookQuantity)
			.build();
	}
}
