package com.nhnacademy.bookstoreback.global.exception;

import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class OrderStatusFailException extends GlobalException {
	public OrderStatusFailException(ErrorStatus errorStatus) {
		super(errorStatus);
	}
}
