package com.nhnacademy.bookstoreback.cart.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class CartNotFoundException extends NotFoundException {
	public CartNotFoundException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 카트 '%s'는 존재하지 않는 카트 입니다.", value),
				HttpStatus.NOT_FOUND,
				LocalDateTime.now()));
	}
}
