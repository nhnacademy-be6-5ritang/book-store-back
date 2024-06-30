package com.nhnacademy.bookstoreback.book.domain.dto.response;

import java.math.BigDecimal;
import java.util.Date;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;

import lombok.Builder;

/**
 * 책 상세페이지 응답 DTO
 *
 * @author 김기욱
 * @version 1.0
 */
@Builder
public record GetBookDetailResponse(
	String authorName,
	String publisherName,
	String bookStatusName,
	String bookTitle,
	String bookDescription,
	String bookIndex,
	boolean bookPackaging,
	int bookQuantity,
	Date bookPublishDate,
	String bookIsbn,
	BigDecimal bookPrice,
	BigDecimal bookSalePrice,
	BigDecimal bookSalePercent) {

	/**
	 * Book 엔티티를 BookDetailResponse DTO로 변환하는 메소드
	 *
	 * @param book Book 엔티티
	 * @return BookDetailResponse DTO
	 */
	public static GetBookDetailResponse fromEntity(Book book) {
		return GetBookDetailResponse.builder()
			.authorName(book.getAuthor().getAuthorName())
			.publisherName(book.getPublisher().getPublisherName())
			.bookStatusName(book.getBookStatus().getBookStatusName())
			.bookTitle(book.getBookTitle())
			.bookDescription(book.getBookDescription())
			.bookIndex(book.getBookIndex())
			.bookPackaging(book.isBookPackaging())
			.bookQuantity(book.getBookQuantity())
			.bookPublishDate(book.getBookPublishDate())
			.bookIsbn(book.getBookIsbn())
			.bookPrice(book.getBookPrice())
			.bookSalePrice(book.getBookSalePrice())
			.bookSalePercent(book.getBookSalePercent())
			.build();
	}
}
