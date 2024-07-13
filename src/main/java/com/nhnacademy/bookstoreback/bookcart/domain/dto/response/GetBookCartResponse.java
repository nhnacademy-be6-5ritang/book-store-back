package com.nhnacademy.bookstoreback.bookcart.domain.dto.response;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.bookcart.domain.entity.BookCart;

import lombok.Builder;

@Builder
public record GetBookCartResponse(
	Long bookCartId,
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
	int bookQuantity
) {
	public static GetBookCartResponse fromEntity(BookCart bookCart) {
		String imageUrl = bookCart.getBook().getBookImages().stream()
			.map(bookImage -> bookImage.getImage().getImageUrl())
			.findFirst()
			.orElse(null); // 이미지가 없는 경우 null 반환

		return GetBookCartResponse.builder()
			.bookCartId(bookCart.getBookCartId())
			.bookId(bookCart.getBook().getBookId())
			.cartId(bookCart.getCart().getCartId())
			.bookImageUrl(imageUrl)
			.bookTitle(bookCart.getBook().getBookTitle())
			.authorName(bookCart.getBook().getAuthor().getAuthorName())
			.publisherName(bookCart.getBook().getPublisher().getPublisherName())
			.bookPrice(bookCart.getBook().getBookPrice())
			.bookSalePrice(bookCart.getBook().getBookSalePrice())
			.bookSalePercent(bookCart.getBook().getBookSalePercent())
			.inventorQuantity(bookCart.getBook().getBookQuantity())
			.bookQuantity(bookCart.getBookQuantity())
			.build();
	}
}
