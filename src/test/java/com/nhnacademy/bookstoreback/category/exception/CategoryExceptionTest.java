package com.nhnacademy.bookstoreback.category.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

class CategoryExceptionTest {

	@Test
	void testCategoryAlreadyExistsException() {
		String categoryName = "Fiction";
		CategoryAlreadyExistsException exception = new CategoryAlreadyExistsException(categoryName);

		ErrorStatus errorStatus = exception.getErrorStatus();
		assertEquals(String.format("해당 도서상태 '%s'는 이미 존재 하는 도서상태 입니다.", categoryName), errorStatus.getMessage());
		assertEquals(HttpStatus.CONFLICT, errorStatus.getStatus());
		assertEquals(LocalDateTime.now().getDayOfYear(),
			errorStatus.getTimestamp().getDayOfYear());
	}

	@Test
	void testCategoryNotFoundException() {
		String categoryName = "NonExistentCategory";
		CategoryNotFoundException exception = new CategoryNotFoundException(categoryName);

		ErrorStatus errorStatus = exception.getErrorStatus();
		assertEquals(String.format("해당 카테고리 '%s'는 이미 존재 하는 카테고리 입니다.", categoryName), errorStatus.getMessage());
		assertEquals(HttpStatus.NOT_FOUND, errorStatus.getStatus());
		assertEquals(LocalDateTime.now().getDayOfYear(),
			errorStatus.getTimestamp().getDayOfYear());
	}
}
