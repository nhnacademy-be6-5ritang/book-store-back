package com.nhnacademy.bookstoreback.tag.domain.dto.respnse;

import com.nhnacademy.bookstoreback.tag.domain.entity.Tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record TagDto(
	@NotNull Long tagId,
	@NotBlank @Size(max = 20) String tagName) {
	public static TagDto fromEntity(Tag tag) {
		return TagDto.builder()
			.tagId(tag.getTagId())
			.tagName(tag.getTagName())
			.build();
	}
}
