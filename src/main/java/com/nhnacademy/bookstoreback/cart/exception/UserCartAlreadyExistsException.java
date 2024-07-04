package com.nhnacademy.bookstoreback.cart.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class UserCartAlreadyExistsException extends AlreadyExistsException {
	public UserCartAlreadyExistsException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 유저 '%s'은 이미 카트를 가지고 있습니다.", value),
				HttpStatus.CONFLICT,
				LocalDateTime.now()));
	}
}
