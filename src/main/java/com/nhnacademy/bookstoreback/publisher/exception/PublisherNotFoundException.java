package com.nhnacademy.bookstoreback.publisher.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class PublisherNotFoundException extends NotFoundException {
	public PublisherNotFoundException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 출판사 '%s'는 존재하지 않는 출판사 입니다.", value),
				HttpStatus.NOT_FOUND,
				LocalDateTime.now()));
	}
}
