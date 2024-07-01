package com.nhnacademy.bookstoreback.usergrade.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

import lombok.Getter;

@Getter
public class UserGradeNotFoundException extends NotFoundException {
	public UserGradeNotFoundException(String userGradeName) {
		super(ErrorStatus.from(
			String.format("해당 회원 등급 '%s'는 존재하지 않는 회원 등급입니다.", userGradeName),
			HttpStatus.CONFLICT,
			LocalDateTime.now()
		));
	}
}
