package com.nhnacademy.bookstoreback.tag.domain.dto.respnse;

import com.nhnacademy.bookstoreback.tag.domain.entity.Tag;

import lombok.Builder;

@Builder
public record TagDto(
	Long tagId,
	String tagName) {
	public static TagDto fromEntity(Tag tag) {
		return TagDto.builder()
			.tagId(tag.getTagId())
			.tagName(tag.getTagName())
			.build();
	}
}
