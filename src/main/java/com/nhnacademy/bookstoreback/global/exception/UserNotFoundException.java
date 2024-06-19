package com.nhnacademy.bookstoreback.global.exception;

import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

import lombok.Getter;

@Getter
public class UserNotFoundException extends GlobalException {
	public UserNotFoundException(ErrorStatus errorStatus) {
		super(errorStatus);
	}
}
