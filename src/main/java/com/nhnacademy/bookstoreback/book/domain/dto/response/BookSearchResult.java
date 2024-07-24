package com.nhnacademy.bookstoreback.book.domain.dto.response;

/**
 * 책 검색결과 응답 DTO
 *
 * @author 이기훈
 */
public record BookSearchResult(
	Long bookId,
	String bookTitle) {
}