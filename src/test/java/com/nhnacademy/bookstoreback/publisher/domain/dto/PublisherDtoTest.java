package com.nhnacademy.bookstoreback.publisher.domain.dto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import com.nhnacademy.bookstoreback.publisher.domain.dto.respnse.PublisherDto;
import com.nhnacademy.bookstoreback.publisher.domain.entity.Publisher;

class PublisherDtoTest {

	@Test
	void testFromEntity() {
		Publisher publisher = mock(Publisher.class);
		when(publisher.getPublisherId()).thenReturn(1L);
		when(publisher.getPublisherName()).thenReturn("Test Publisher");

		PublisherDto publisherDto = PublisherDto.fromEntity(publisher);
		
		assertEquals(1L, publisherDto.publisherId());
		assertEquals("Test Publisher", publisherDto.publisherName());
	}
}