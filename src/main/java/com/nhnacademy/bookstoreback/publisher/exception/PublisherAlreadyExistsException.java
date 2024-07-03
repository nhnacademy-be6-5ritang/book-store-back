package com.nhnacademy.bookstoreback.publisher.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class PublisherAlreadyExistsException extends AlreadyExistsException {
	public PublisherAlreadyExistsException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 출판사 '%s'는 이미 존재 하는 출판사 입니다.", value),
				HttpStatus.CONFLICT,
				LocalDateTime.now()));
	}
}
