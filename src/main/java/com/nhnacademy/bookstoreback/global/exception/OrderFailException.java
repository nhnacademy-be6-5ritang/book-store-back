package com.nhnacademy.bookstoreback.global.exception;

import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class OrderFailException extends GlobalException {
	public OrderFailException(ErrorStatus errorStatus) {
		super(errorStatus);
	}
}
