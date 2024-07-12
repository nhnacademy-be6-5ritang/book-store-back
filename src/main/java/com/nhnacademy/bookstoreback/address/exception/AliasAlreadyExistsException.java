package com.nhnacademy.bookstoreback.address.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class AliasAlreadyExistsException extends AlreadyExistsException {
	public AliasAlreadyExistsException(String alias) {
		super(ErrorStatus.from(
			String.format("해당 별칭 '%s'는 이미 존재하는 별칭입니다.", alias),
			HttpStatus.CONFLICT,
			LocalDateTime.now()
		));
	}
}
