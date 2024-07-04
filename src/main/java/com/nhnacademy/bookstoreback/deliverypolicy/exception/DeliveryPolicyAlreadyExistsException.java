package com.nhnacademy.bookstoreback.deliverypolicy.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class DeliveryPolicyAlreadyExistsException extends AlreadyExistsException {
	public DeliveryPolicyAlreadyExistsException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 배송비정책 '%s'는 이미 존재 하는 배송비정책 입니다.", value),
				HttpStatus.CONFLICT,
				LocalDateTime.now()));
	}
}
