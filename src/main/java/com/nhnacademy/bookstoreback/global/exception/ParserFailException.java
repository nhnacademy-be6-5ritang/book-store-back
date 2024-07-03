package com.nhnacademy.bookstoreback.global.exception;

import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class ParserFailException extends GlobalException {
	public ParserFailException(ErrorStatus errorStatus) {
		super(errorStatus);
	}
}
