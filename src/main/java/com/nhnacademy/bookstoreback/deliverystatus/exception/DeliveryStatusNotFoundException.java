package com.nhnacademy.bookstoreback.deliverystatus.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class DeliveryStatusNotFoundException extends NotFoundException {
	public DeliveryStatusNotFoundException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 배송상태 '%s'는 존재하지 않는 배송상태 입니다.", value),
				HttpStatus.NOT_FOUND,
				LocalDateTime.now()));
	}
}
