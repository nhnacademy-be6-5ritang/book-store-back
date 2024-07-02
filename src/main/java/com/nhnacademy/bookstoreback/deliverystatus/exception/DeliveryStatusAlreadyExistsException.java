package com.nhnacademy.bookstoreback.deliverystatus.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class DeliveryStatusAlreadyExistsException extends AlreadyExistsException {
	public DeliveryStatusAlreadyExistsException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 배송상태 '%s'는 이미 존재 하는 배송상태 입니다.", value),
				HttpStatus.CONFLICT,
				LocalDateTime.now()));
	}
}
