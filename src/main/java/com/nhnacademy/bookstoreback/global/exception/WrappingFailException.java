package com.nhnacademy.bookstoreback.global.exception;

import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class WrappingFailException extends GlobalException {
	public WrappingFailException(ErrorStatus errorStatus) {
		super(errorStatus);
	}
}
