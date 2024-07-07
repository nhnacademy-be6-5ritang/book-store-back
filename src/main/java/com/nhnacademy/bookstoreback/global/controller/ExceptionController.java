package com.nhnacademy.bookstoreback.global.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.nhnacademy.bookstoreback.global.exception.GlobalException;

@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(GlobalException.class)
	public ResponseEntity<String> handleException(GlobalException exception) {
		return ResponseEntity.status(exception.getErrorStatus().getStatus())
			.body(exception.getErrorStatus().getMessage());
	}
}
