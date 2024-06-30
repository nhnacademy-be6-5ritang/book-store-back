package com.nhnacademy.bookstoreback.publisher.domain.dto.respnse;

import com.nhnacademy.bookstoreback.publisher.domain.entity.Publisher;

import lombok.Builder;

@Builder
public record PublisherDto(
	Long publisherId,
	String publisherName) {
	public static PublisherDto fromEntity(Publisher publisher) {
		return PublisherDto.builder()
			.publisherId(publisher.getPublisherId())
			.publisherName(publisher.getPublisherName())
			.build();
	}
}
