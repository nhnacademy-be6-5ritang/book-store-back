package com.nhnacademy.bookstoreback.review.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

class ReviewExceptionTest {
	@Test
	void testReviewAlreadyExistsException() {
		String value = "testWishList";
		ReviewAlreadyExistsException exception = new ReviewAlreadyExistsException(value);

		ErrorStatus errorStatus = exception.getErrorStatus();

		assertEquals(HttpStatus.CONFLICT, errorStatus.getStatus());
		assertEquals(String.format("해당 위시리스트 '%s'는 이미 존재 하는 위시리스트 입니다.", value), errorStatus.getMessage());
		assertTrue(errorStatus.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
	}

	@Test
	void testReviewNotFoundException() {
		String value = "testWishList";
		ReviewNotFoundException exception = new ReviewNotFoundException(value);

		ErrorStatus errorStatus = exception.getErrorStatus();

		assertEquals(HttpStatus.NOT_FOUND, errorStatus.getStatus());
		assertEquals(String.format("해당 위시리스트 '%s'는 존재하지 않는 위시리스트 입니다.", value), errorStatus.getMessage());
		assertTrue(errorStatus.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
	}
}
