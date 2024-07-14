package com.nhnacademy.bookstoreback.upload.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.GlobalException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class ParserException extends GlobalException {
	public ParserException() {
		super(
			ErrorStatus.from(String.format("업로드 파일을 파싱하는데 오류가 발생하였습니다."),
				HttpStatus.INTERNAL_SERVER_ERROR,
				LocalDateTime.now()));
	}
}
