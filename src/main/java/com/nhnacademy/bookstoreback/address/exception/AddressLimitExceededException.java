package com.nhnacademy.bookstoreback.address.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.GlobalException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class AddressLimitExceededException extends GlobalException {
	public AddressLimitExceededException() {
		super(ErrorStatus.from(
			"주소의 개수가 10개 초과했습니다.",
			HttpStatus.BAD_REQUEST,
			LocalDateTime.now()
		));
	}
}
