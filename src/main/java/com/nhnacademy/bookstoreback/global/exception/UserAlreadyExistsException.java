package com.nhnacademy.bookstoreback.global.exception;

import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends GlobalException {
	public UserAlreadyExistsException(ErrorStatus errorStatus) {
		super(errorStatus);
	}
}
