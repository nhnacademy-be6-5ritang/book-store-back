package com.nhnacademy.bookstoreback.book.domain.dto.response;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;

import lombok.Builder;

/**
 * 책 상세페이지 응답 DTO
 *
 * @author 김기욱
 * @version 1.0
 */
@Builder
public record GetBookTitleResponse(
	Long bookId,
	String bookTitle) {

	public static GetBookTitleResponse fromEntity(Book book) {
		return GetBookTitleResponse.builder()
			.bookId(book.getBookId())
			.bookTitle(book.getBookTitle())
			.build();
	}
}
