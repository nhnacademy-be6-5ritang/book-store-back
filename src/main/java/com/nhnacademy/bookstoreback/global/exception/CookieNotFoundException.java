package com.nhnacademy.bookstoreback.global.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class CookieNotFoundException extends NotFoundException {
	public CookieNotFoundException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 쿠키 '%s'는 존재하지 않는 쿠키 입니다.", value),
				HttpStatus.NOT_FOUND,
				LocalDateTime.now()));
	}
}
