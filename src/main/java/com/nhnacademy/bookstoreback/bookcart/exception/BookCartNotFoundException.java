package com.nhnacademy.bookstoreback.bookcart.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class BookCartNotFoundException extends NotFoundException {
	public BookCartNotFoundException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 도서카트 '%s'는 존재하지 않는 도서카트 입니다.", value),
				HttpStatus.NOT_FOUND,
				LocalDateTime.now()));
	}
}
