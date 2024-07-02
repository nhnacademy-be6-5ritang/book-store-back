package com.nhnacademy.bookstoreback.userrole.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class UserHasRoleAlreadyException extends AlreadyExistsException {
	public UserHasRoleAlreadyException(Long userId, String roleName) {
		super(ErrorStatus.from(
			String.format("해당 사용자 '%d'는 이미 '%s' 역할을 가지고 있습니다.", userId, roleName),
			HttpStatus.CONFLICT,
			LocalDateTime.now()
		));
	}
}
