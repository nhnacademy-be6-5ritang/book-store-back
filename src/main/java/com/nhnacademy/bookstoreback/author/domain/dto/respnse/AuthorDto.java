package com.nhnacademy.bookstoreback.author.domain.dto.respnse;

import com.nhnacademy.bookstoreback.author.domain.entity.Author;

import lombok.Builder;

@Builder
public record AuthorDto(
	Long authorId,
	String authorName) {
	public static AuthorDto fromEntity(Author author) {
		return AuthorDto.builder()
			.authorId(author.getAuthorId())
			.authorName(author.getAuthorName())
			.build();
	}
}
