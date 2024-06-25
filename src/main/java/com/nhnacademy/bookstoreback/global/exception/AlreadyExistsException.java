package com.nhnacademy.bookstoreback.global.exception;

import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

import lombok.Getter;

@Getter
public class AlreadyExistsException extends GlobalException {
	public AlreadyExistsException(ErrorStatus errorStatus) {
		super(errorStatus);
	}
}
