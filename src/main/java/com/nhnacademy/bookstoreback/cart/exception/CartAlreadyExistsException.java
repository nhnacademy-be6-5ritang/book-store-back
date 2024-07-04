package com.nhnacademy.bookstoreback.cart.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class CartAlreadyExistsException extends AlreadyExistsException {
	public CartAlreadyExistsException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 카트 '%s'는 이미 존재 하는 카트 입니다.", value),
				HttpStatus.CONFLICT,
				LocalDateTime.now()));
	}
}
