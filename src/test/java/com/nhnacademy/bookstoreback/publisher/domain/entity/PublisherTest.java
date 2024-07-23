package com.nhnacademy.bookstoreback.publisher.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nhnacademy.bookstoreback.publisher.domain.dto.respnse.PublisherDto;

class PublisherTest {

	private Publisher publisher;

	@BeforeEach
	void setUp() {
		publisher = Publisher.builder()
			.publisherName("Test Publisher")
			.build();
	}

	@Test
	void testPublisherBuilder() {
		Publisher testPublisher = Publisher.builder()
			.publisherName("Test Publisher")
			.build();
		assertNull(testPublisher.getPublisherId(), "reviewId should be null upon creation");
	}

	@Test
	void testNoArgsConstructorIsProtected() {
		try {
			Constructor<Publisher> constructor = Publisher.class.getDeclaredConstructor();
			assertTrue(java.lang.reflect.Modifier.isProtected(constructor.getModifiers()),
				"Default constructor is not protected");
		} catch (NoSuchMethodException e) {
			fail("No default constructor found");
		}
		new Publisher();
	}

	@Test
	void testPublisherCreation() {
		assertNotNull(publisher);
		assertEquals("Test Publisher", publisher.getPublisherName());
	}

	@Test
	void testToEntity() {
		PublisherDto dto = new PublisherDto(1L, "DTO Publisher");

		Publisher publisherFromDto = Publisher.toEntity(dto);

		assertNotNull(publisherFromDto);
		assertEquals("DTO Publisher", publisherFromDto.getPublisherName());
	}

	@Test
	void testUpdatePublisherName() {
		publisher.updatePublisherName("Updated Publisher");

		assertEquals("Updated Publisher", publisher.getPublisherName());
	}
}
