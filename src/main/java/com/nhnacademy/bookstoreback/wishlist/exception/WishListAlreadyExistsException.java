package com.nhnacademy.bookstoreback.wishlist.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class WishListAlreadyExistsException extends AlreadyExistsException {
	public WishListAlreadyExistsException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 위시리스트 '%s'는 이미 존재 하는 위시리스트 입니다.", value),
				HttpStatus.CONFLICT,
				LocalDateTime.now()));
	}
}
