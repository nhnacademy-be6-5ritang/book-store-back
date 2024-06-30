package com.nhnacademy.bookstoreback.bookstatus.domain.dto.respnse;

import com.nhnacademy.bookstoreback.bookstatus.domain.entity.BookStatus;

import lombok.Builder;

@Builder
public record BookStatusDto(
	Long bookStatusId,
	String bookStatusName) {
	public static BookStatusDto fromEntity(BookStatus bookStatus) {
		return BookStatusDto.builder()
			.bookStatusId(bookStatus.getBookStatusId())
			.bookStatusName(bookStatus.getBookStatusName())
			.build();
	}
}
