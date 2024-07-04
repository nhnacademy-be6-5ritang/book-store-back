package com.nhnacademy.bookstoreback.category.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class CategoryAlreadyExistsException extends AlreadyExistsException {
	public CategoryAlreadyExistsException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 도서상태 '%s'는 이미 존재 하는 도서상태 입니다.", value),
				HttpStatus.CONFLICT,
				LocalDateTime.now()));
	}
}
