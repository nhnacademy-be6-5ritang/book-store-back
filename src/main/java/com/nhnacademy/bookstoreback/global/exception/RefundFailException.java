package com.nhnacademy.bookstoreback.global.exception;

import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class RefundFailException extends GlobalException {
	public RefundFailException(ErrorStatus errorStatus) {
		super(errorStatus);
	}
}
