package com.nhnacademy.bookstoreback.role.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class RoleAlreadyExistsException extends AlreadyExistsException {
	public RoleAlreadyExistsException(String roleName) {
		super(ErrorStatus.from(
			String.format("해당 역할 '%s'는 이미 존재하는 역할입니다.", roleName),
			HttpStatus.CONFLICT,
			LocalDateTime.now()
		));
	}
}
