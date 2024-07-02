package com.nhnacademy.bookstoreback.user.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

import lombok.Getter;

@Getter
public class UserNotFoundException extends NotFoundException {
	public UserNotFoundException(Long userId) {
		super(ErrorStatus.from(
			String.format("해당 사용자 '%d'는 존재하지 않는 사용자입니다.", userId),
			HttpStatus.NOT_FOUND,
			LocalDateTime.now()
		));
	}
}
