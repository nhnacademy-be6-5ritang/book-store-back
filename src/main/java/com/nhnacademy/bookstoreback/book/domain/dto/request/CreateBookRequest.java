package com.nhnacademy.bookstoreback.book.domain.dto.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;

import lombok.Builder;

/**
 * 책 상세페이지 응답 DTO
 *
 * @author 김기욱
 * @version 1.0
 */
@Builder
public record CreateBookRequest(
	String bookIsbn,
	List<Long> categories,
	List<Long> tags,
	String bookTitle,
	String authorName,
	String publisherName,
	Date bookPublishDate,
	String bookStatusName,
	String bookIndex,
	String bookDescription,
	int bookQuantity,
	boolean bookPackaging,
	BigDecimal bookPrice,
	BigDecimal bookSalePrice,
	BigDecimal bookSalePercent) {

	public static CreateBookRequest fromEntity(Book book) {
		return CreateBookRequest.builder()
			.bookIsbn(book.getBookIsbn())
			.bookTitle(book.getBookTitle())
			.authorName(book.getAuthor().getAuthorName())
			.publisherName(book.getPublisher().getPublisherName())
			.bookPublishDate(book.getBookPublishDate())
			.bookStatusName(book.getBookStatus().getBookStatusName())
			.bookIndex(book.getBookIndex())
			.bookDescription(book.getBookDescription())
			.bookQuantity(book.getBookQuantity())
			.bookPackaging(book.isBookPackaging())
			.bookPrice(book.getBookPrice())
			.bookSalePrice(book.getBookSalePrice())
			.bookSalePercent(book.getBookSalePercent())
			.build();
	}
}
