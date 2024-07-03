package com.nhnacademy.bookstoreback.author.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class AuthorAlreadyExistsException extends AlreadyExistsException {
	public AuthorAlreadyExistsException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 저자 '%s'는 이미 존재 하는 저자 입니다.", value),
				HttpStatus.CONFLICT,
				LocalDateTime.now()));
	}
}
