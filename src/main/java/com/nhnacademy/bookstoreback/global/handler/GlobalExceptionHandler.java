package com.nhnacademy.bookstoreback.global.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.nhnacademy.bookstoreback.global.exception.GlobalException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(GlobalException.class)
	public ResponseEntity<ErrorStatus> handleExceptionGlobally(GlobalException ex) {
		ErrorStatus errorStatus = ex.getErrorStatus();
		log.error("프론트 서버 요청에 대한 에러: {}", ex.getMessage());
		return new ResponseEntity<>(errorStatus, errorStatus.getStatus());
	}

	/**
	 * 예외를 처리하고 로그를 남깁니다.
	 *
	 * @param ex 발생한 예외 객체
	 */
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Void> handleException(Exception ex) {
		log.error("서버 에러: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}
