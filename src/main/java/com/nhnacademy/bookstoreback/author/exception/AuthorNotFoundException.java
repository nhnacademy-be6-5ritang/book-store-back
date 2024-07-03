package com.nhnacademy.bookstoreback.author.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class AuthorNotFoundException extends NotFoundException {
	public AuthorNotFoundException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 저자 '%s'는 존재하지 않는 저자 입니다.", value),
				HttpStatus.NOT_FOUND,
				LocalDateTime.now()));
	}
}
