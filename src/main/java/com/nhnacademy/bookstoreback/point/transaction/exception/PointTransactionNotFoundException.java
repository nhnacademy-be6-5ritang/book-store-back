package com.nhnacademy.bookstoreback.point.transaction.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class PointTransactionNotFoundException extends AlreadyExistsException {
	public PointTransactionNotFoundException() {
		super(ErrorStatus.from(
			"포인트 거래량이 존재하지 않습니다.",
			HttpStatus.NOT_FOUND,
			LocalDateTime.now()
		));
	}
}
