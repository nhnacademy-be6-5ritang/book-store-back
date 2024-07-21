package com.nhnacademy.bookstoreback.author.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

class AuthorExceptionTest {

	@Test
	void testAuthorAlreadyExistsException() {
		String authorName = "John Doe";
		AuthorAlreadyExistsException exception = new AuthorAlreadyExistsException(authorName);

		ErrorStatus errorStatus = exception.getErrorStatus();

		assertEquals(String.format("해당 저자 '%s'는 이미 존재 하는 저자 입니다.", authorName), errorStatus.getMessage());
		assertEquals(HttpStatus.CONFLICT, errorStatus.getStatus());
		assertEquals(LocalDateTime.now().getYear(), errorStatus.getTimestamp().getYear());
	}

	@Test
	void testAuthorNotFoundException() {
		String authorName = "Jane Doe";
		AuthorNotFoundException exception = new AuthorNotFoundException(authorName);

		ErrorStatus errorStatus = exception.getErrorStatus();

		assertEquals(String.format("해당 저자 '%s'는 존재하지 않는 저자 입니다.", authorName), errorStatus.getMessage());
		assertEquals(HttpStatus.NOT_FOUND, errorStatus.getStatus());
		assertEquals(LocalDateTime.now().getYear(), errorStatus.getTimestamp().getYear());
	}
}