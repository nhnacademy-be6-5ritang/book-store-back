package com.nhnacademy.bookstoreback.book.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class BookNotFoundException extends NotFoundException {
	public BookNotFoundException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 도서 '%s'는 존재하지 않는 도서 입니다.", value),
				HttpStatus.NOT_FOUND,
				LocalDateTime.now()));
	}
}
