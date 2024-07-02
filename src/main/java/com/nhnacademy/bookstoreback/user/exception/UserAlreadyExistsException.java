package com.nhnacademy.bookstoreback.user.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends AlreadyExistsException {
	public UserAlreadyExistsException(Long userId) {
		super(ErrorStatus.from(
			String.format("해당 사용자 '%d'는 이미 존재하는 사용자입니다.", userId),
			HttpStatus.CONFLICT,
			LocalDateTime.now()
		));
	}

	public UserAlreadyExistsException(String email) {
		super(ErrorStatus.from(
			String.format("해당 이메일 '%s'는 이미 존재하는 사용자의 이메일입니다.", email),
			HttpStatus.CONFLICT,
			LocalDateTime.now()
		));
	}
}
