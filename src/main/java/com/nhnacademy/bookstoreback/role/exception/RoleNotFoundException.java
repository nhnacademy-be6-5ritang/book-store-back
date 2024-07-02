package com.nhnacademy.bookstoreback.role.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class RoleNotFoundException extends AlreadyExistsException {
	public RoleNotFoundException(Long roleId) {
		super(ErrorStatus.from(
			String.format("해당 권한 ID '%d'는 이미 존재하는 권한입니다.", roleId),
			HttpStatus.CONFLICT,
			LocalDateTime.now()
		));
	}

	public RoleNotFoundException(String roleName) {
		super(ErrorStatus.from(
			String.format("해당 권한 '%s'는 이미 존재하는 권한입니다.", roleName),
			HttpStatus.CONFLICT,
			LocalDateTime.now()
		));
	}
}
