package com.nhnacademy.bookstoreback.global.exception;

import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class BookOrderFailException extends GlobalException {
	public BookOrderFailException(ErrorStatus errorStatus) {
		super(errorStatus);
	}
}
