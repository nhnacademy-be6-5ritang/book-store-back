package com.nhnacademy.bookstoreback.global.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class CookieAlreadyExistsException extends AlreadyExistsException {
	public CookieAlreadyExistsException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 쿠키 '%s'는 이미 존재 하는 쿠키 입니다.", value),
				HttpStatus.CONFLICT,
				LocalDateTime.now()));
	}
}
