package com.nhnacademy.bookstoreback.deliverypolicy.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class DeliveryPolicyNotFoundException extends NotFoundException {
	public DeliveryPolicyNotFoundException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 배송비정책 '%s'는 존재하지 않는 배송비정책 입니다.", value),
				HttpStatus.NOT_FOUND,
				LocalDateTime.now()));
	}
}
