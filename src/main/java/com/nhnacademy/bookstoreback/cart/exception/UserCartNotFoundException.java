package com.nhnacademy.bookstoreback.cart.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class UserCartNotFoundException extends NotFoundException {
	public UserCartNotFoundException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 유저 '%s'는 카트를 가지고 있지 않습니다.", value),
				HttpStatus.NOT_FOUND,
				LocalDateTime.now()));
	}
}
