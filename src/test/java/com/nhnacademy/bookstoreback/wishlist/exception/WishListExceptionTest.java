package com.nhnacademy.bookstoreback.wishlist.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

class WishListExceptionTest {

	@Test
	void testWishListNotFoundException() {
		String value = "456";

		WishListNotFoundException exception = assertThrows(WishListNotFoundException.class, () -> {
			throw new WishListNotFoundException(value);
		});

		ErrorStatus errorStatus = exception.getErrorStatus();

		assertEquals("해당 위시리스트 '456'는 존재하지 않는 위시리스트 입니다.", errorStatus.getMessage());
		assertEquals(HttpStatus.NOT_FOUND, errorStatus.getStatus());
	}

	@Test
	void testWishListAlreadyExistsException() {
		String value = "123";

		WishListAlreadyExistsException exception = assertThrows(WishListAlreadyExistsException.class, () -> {
			throw new WishListAlreadyExistsException(value);
		});

		ErrorStatus errorStatus = exception.getErrorStatus();

		assertEquals("해당 위시리스트 '123'는 이미 존재 하는 위시리스트 입니다.", errorStatus.getMessage());
		assertEquals(HttpStatus.CONFLICT, errorStatus.getStatus());
	}

}
