package com.nhnacademy.bookstoreback.global.exception;

import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

import lombok.Getter;

@Getter
public class NotFoundException extends GlobalException {
	public NotFoundException(ErrorStatus errorStatus) {
		super(errorStatus);
	}
}
