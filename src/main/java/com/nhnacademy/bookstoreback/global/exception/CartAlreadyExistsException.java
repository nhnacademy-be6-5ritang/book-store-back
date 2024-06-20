package com.nhnacademy.bookstoreback.global.exception;

import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

import lombok.Getter;

@Getter
public class CartAlreadyExistsException extends GlobalException {
	public CartAlreadyExistsException(ErrorStatus errorStatus) {
		super(errorStatus);
	}
}
