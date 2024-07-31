package com.nhnacademy.bookstoreback.book.exception;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

public class BookNotFoundExceptionTest {

	@Test
	void testBookNotFoundException() {
		// Given
		String bookTitle = "존재하지 않는 책";

		// When
		BookNotFoundException exception = new BookNotFoundException(bookTitle);

		// Then
		assertEquals(HttpStatus.NOT_FOUND, exception.getErrorStatus().getStatus());
		assertTrue(exception.getErrorStatus().getMessage().contains(bookTitle));
		assertNotNull(exception.getErrorStatus().getTimestamp());
	}
}
