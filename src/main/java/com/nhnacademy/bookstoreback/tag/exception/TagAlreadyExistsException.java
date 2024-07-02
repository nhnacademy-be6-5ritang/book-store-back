package com.nhnacademy.bookstoreback.tag.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class TagAlreadyExistsException extends AlreadyExistsException {
	public TagAlreadyExistsException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 태그 '%s'는 이미 존재 하는 태그 입니다.", value),
				HttpStatus.CONFLICT,
				LocalDateTime.now()));
	}
}
