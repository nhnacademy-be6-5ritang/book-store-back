package com.nhnacademy.bookstoreback.global.exception;

import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class PaymentFailException extends GlobalException {
	public PaymentFailException(ErrorStatus errorStatus) {
		super(errorStatus);
	}
}
