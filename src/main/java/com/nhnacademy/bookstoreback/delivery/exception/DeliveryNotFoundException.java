package com.nhnacademy.bookstoreback.delivery.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class DeliveryNotFoundException extends NotFoundException {
	public DeliveryNotFoundException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 배송 '%s'는 존재하지 않는 배송 입니다.", value),
				HttpStatus.NOT_FOUND,
				LocalDateTime.now()));
	}
}
