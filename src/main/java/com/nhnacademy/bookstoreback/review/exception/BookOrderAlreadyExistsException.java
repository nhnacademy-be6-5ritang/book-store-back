package com.nhnacademy.bookstoreback.review.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class BookOrderAlreadyExistsException extends AlreadyExistsException {
	public BookOrderAlreadyExistsException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 도서주문 '%s'는 이미 존재 하는 도서주문 입니다.", value),
				HttpStatus.CONFLICT,
				LocalDateTime.now()));
	}
}
