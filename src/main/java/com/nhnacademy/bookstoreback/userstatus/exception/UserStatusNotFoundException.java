package com.nhnacademy.bookstoreback.userstatus.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class UserStatusNotFoundException extends AlreadyExistsException {
	public UserStatusNotFoundException(String userStatusName) {
		super(ErrorStatus.from(
			String.format("해당 사용자 상태 '%s'는 존재하지 않는 사용자 상태입니다.", userStatusName),
			HttpStatus.CONFLICT,
			LocalDateTime.now()
		));
	}
}
