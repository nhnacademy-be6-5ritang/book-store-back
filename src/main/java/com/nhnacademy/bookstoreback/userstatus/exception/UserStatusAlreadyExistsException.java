package com.nhnacademy.bookstoreback.userstatus.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class UserStatusAlreadyExistsException extends AlreadyExistsException {
	public UserStatusAlreadyExistsException(String userStatusName) {
		super(ErrorStatus.from(
			String.format("해당 사용자 상태 '%s'는 이미 존재하는 사용자 상태입니다.", userStatusName),
			HttpStatus.CONFLICT,
			LocalDateTime.now()
		));
	}
}
