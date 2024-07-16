package com.nhnacademy.bookstoreback.bookstatus.domain.dto.respnse;

import com.nhnacademy.bookstoreback.bookstatus.domain.entity.BookStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record BookStatusDto(
	@NotNull Long bookStatusId,
	@NotBlank @Size(max = 10) String bookStatusName) {
	public static BookStatusDto fromEntity(BookStatus bookStatus) {
		return BookStatusDto.builder()
			.bookStatusId(bookStatus.getBookStatusId())
			.bookStatusName(bookStatus.getBookStatusName())
			.build();
	}
}
