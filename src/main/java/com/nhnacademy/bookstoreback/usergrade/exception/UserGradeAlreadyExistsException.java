package com.nhnacademy.bookstoreback.usergrade.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class UserGradeAlreadyExistsException extends AlreadyExistsException {
	public UserGradeAlreadyExistsException(String userGradeName) {
		super(ErrorStatus.from(
			String.format("해당 회원 등급명 '%s'는 이미 존재하는 회원 등급명입니다.", userGradeName),
			HttpStatus.CONFLICT,
			LocalDateTime.now()
		));
	}
}
