package com.nhnacademy.bookstoreback.delivery.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class DeliveryByOrderNotFoundException extends NotFoundException {
	public DeliveryByOrderNotFoundException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 주문 %s 에대한 배송이 존재하지 않습니다.", value),
				HttpStatus.NOT_FOUND,
				LocalDateTime.now()));
	}
}
