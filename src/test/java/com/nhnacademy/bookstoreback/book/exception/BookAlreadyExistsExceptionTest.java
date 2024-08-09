package com.nhnacademy.bookstoreback.book.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class BookAlreadyExistsExceptionTest {

	@Test
	void testBookAlreadyExistsException() {
		// Given
		String bookTitle = "자바의 정석";

		// When
		BookAlreadyExistsException exception = new BookAlreadyExistsException(bookTitle);

		// Then
		assertEquals(HttpStatus.CONFLICT, exception.getErrorStatus().getStatus());
		assertTrue(exception.getErrorStatus().getMessage().contains(bookTitle));
		assertNotNull(exception.getErrorStatus().getTimestamp());
	}
}
