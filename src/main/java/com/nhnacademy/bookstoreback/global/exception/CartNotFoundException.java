package com.nhnacademy.bookstoreback.global.exception;

import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

import lombok.Getter;

@Getter
public class CartNotFoundException extends GlobalException {
	public CartNotFoundException(ErrorStatus errorStatus) {
		super(errorStatus);
	}
}
