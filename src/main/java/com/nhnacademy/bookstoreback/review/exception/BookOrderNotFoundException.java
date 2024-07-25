package com.nhnacademy.bookstoreback.review.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class BookOrderNotFoundException extends NotFoundException {
	public BookOrderNotFoundException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 도서주문 '%s'는 존재하지 않는 도서주문 입니다.", value),
				HttpStatus.NOT_FOUND,
				LocalDateTime.now()));
	}
}
