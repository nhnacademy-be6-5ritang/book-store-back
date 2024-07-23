package com.nhnacademy.bookstoreback.bookstatus.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

class BookStatusExceptionTest {

	@Test
	void testBookStatusAlreadyExistsException() {
		String status = "Available";
		BookStatusAlreadyExistsException exception = new BookStatusAlreadyExistsException(status);

		ErrorStatus errorStatus = exception.getErrorStatus();

		assertEquals(String.format("해당 도서상태 '%s'는 이미 존재 하는 도서상태 입니다.", status), errorStatus.getMessage());
		assertEquals(HttpStatus.CONFLICT, errorStatus.getStatus());
	}

	@Test
	void testBookStatusNotFoundException() {
		String status = "Unavailable";
		BookStatusNotFoundException exception = new BookStatusNotFoundException(status);

		ErrorStatus errorStatus = exception.getErrorStatus();

		assertEquals(String.format("해당 도서상태 '%s'는 존재하지 않는 도서상태 입니다.", status), errorStatus.getMessage());
		assertEquals(HttpStatus.NOT_FOUND, errorStatus.getStatus());
	}
}
