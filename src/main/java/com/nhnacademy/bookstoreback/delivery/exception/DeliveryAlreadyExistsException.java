package com.nhnacademy.bookstoreback.delivery.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class DeliveryAlreadyExistsException extends AlreadyExistsException {
	public DeliveryAlreadyExistsException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 배송 '%s'는 이미 존재 하는 배송 입니다.", value),
				HttpStatus.CONFLICT,
				LocalDateTime.now()));
	}
}
