package com.nhnacademy.bookstoreback.global.exception;

import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class PaperFailException extends GlobalException {
	public PaperFailException(ErrorStatus errorStatus) {
		super(errorStatus);
	}
}
