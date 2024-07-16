package com.nhnacademy.bookstoreback.publisher.domain.dto.respnse;

import com.nhnacademy.bookstoreback.publisher.domain.entity.Publisher;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record PublisherDto(
	@NotNull Long publisherId,
	@NotBlank @Size(max = 100) String publisherName) {
	public static PublisherDto fromEntity(Publisher publisher) {
		return PublisherDto.builder()
			.publisherId(publisher.getPublisherId())
			.publisherName(publisher.getPublisherName())
			.build();
	}
}
