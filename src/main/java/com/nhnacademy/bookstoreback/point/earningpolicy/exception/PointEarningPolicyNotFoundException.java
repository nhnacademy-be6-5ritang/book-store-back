package com.nhnacademy.bookstoreback.point.earningpolicy.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class PointEarningPolicyNotFoundException extends AlreadyExistsException {
	public PointEarningPolicyNotFoundException(Long pointEarningPolicyId) {
		super(ErrorStatus.from(
			String.format("해당 포인트 적립 정책 ID %d가 존재하지 않습니다.", pointEarningPolicyId),
			HttpStatus.NOT_FOUND,
			LocalDateTime.now()
		));
	}
}
