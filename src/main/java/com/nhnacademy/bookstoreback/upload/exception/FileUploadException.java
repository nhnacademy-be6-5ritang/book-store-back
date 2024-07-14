package com.nhnacademy.bookstoreback.upload.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.GlobalException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class FileUploadException extends GlobalException {
	public FileUploadException(Object value) {
		super(
			ErrorStatus.from(String.format("파일을 업로드하는중에 오류가 발생하였습니다.: %s", value),
				HttpStatus.INTERNAL_SERVER_ERROR,
				LocalDateTime.now()));
	}
}
