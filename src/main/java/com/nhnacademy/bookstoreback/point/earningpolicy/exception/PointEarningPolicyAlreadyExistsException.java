package com.nhnacademy.bookstoreback.point.earningpolicy.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class PointEarningPolicyAlreadyExistsException extends AlreadyExistsException {
	public PointEarningPolicyAlreadyExistsException(String pointEarningPolicyType) {
		super(ErrorStatus.from(
			String.format("해당 포인트 적립 정책 %s은(는) 이미 존재합니다.", pointEarningPolicyType),
			HttpStatus.CONFLICT,
			LocalDateTime.now()
		));
	}
}
