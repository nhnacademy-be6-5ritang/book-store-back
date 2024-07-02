package com.nhnacademy.bookstoreback.tag.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class TagNotFoundException extends NotFoundException {
	public TagNotFoundException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 태그 '%s'는 존재하지 않는 태그 입니다.", value),
				HttpStatus.NOT_FOUND,
				LocalDateTime.now()));
	}
}
