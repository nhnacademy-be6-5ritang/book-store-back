package com.nhnacademy.bookstoreback.book.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class BookAlreadyExistsException extends AlreadyExistsException {
	public BookAlreadyExistsException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 도서 '%s'는 이미 존재 하는 도서 입니다.", value),
				HttpStatus.CONFLICT,
				LocalDateTime.now()));
	}
}
