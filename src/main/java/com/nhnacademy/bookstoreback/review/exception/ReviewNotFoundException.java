package com.nhnacademy.bookstoreback.review.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class ReviewNotFoundException extends NotFoundException {
	public ReviewNotFoundException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 위시리스트 '%s'는 존재하지 않는 위시리스트 입니다.", value),
				HttpStatus.NOT_FOUND,
				LocalDateTime.now()));
	}
}
