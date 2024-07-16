package com.nhnacademy.bookstoreback.author.domain.dto.respnse;

import com.nhnacademy.bookstoreback.author.domain.entity.Author;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record AuthorDto(
	@NotNull Long authorId,
	@NotBlank @Size(max = 200) String authorName) {
	public static AuthorDto fromEntity(Author author) {
		return AuthorDto.builder()
			.authorId(author.getAuthorId())
			.authorName(author.getAuthorName())
			.build();
	}
}
