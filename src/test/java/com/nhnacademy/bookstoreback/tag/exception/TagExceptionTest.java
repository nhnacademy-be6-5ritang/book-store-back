package com.nhnacademy.bookstoreback.tag.exception;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

class TagExceptionTest {

	@Test
	void testTagAlreadyExistsException() {
		String tagName = "existing-tag";
		TagAlreadyExistsException exception = new TagAlreadyExistsException(tagName);

		assertThat(exception).isInstanceOf(AlreadyExistsException.class);
		ErrorStatus errorStatus = exception.getErrorStatus();
		assertThat(errorStatus).isNotNull();
		assertThat(errorStatus.getMessage()).isEqualTo(String.format("해당 태그 '%s'는 이미 존재 하는 태그 입니다.", tagName));
		assertThat(errorStatus.getStatus()).isEqualTo(HttpStatus.CONFLICT);
		assertThat(errorStatus.getTimestamp()).isBeforeOrEqualTo(LocalDateTime.now());
	}

	@Test
	void testTagNotFoundException() {
		String tagName = "non-existing-tag";
		TagNotFoundException exception = new TagNotFoundException(tagName);

		assertThat(exception).isInstanceOf(NotFoundException.class);
		ErrorStatus errorStatus = exception.getErrorStatus();
		assertThat(errorStatus).isNotNull();
		assertThat(errorStatus.getMessage()).isEqualTo(String.format("해당 태그 '%s'는 존재하지 않는 태그 입니다.", tagName));
		assertThat(errorStatus.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(errorStatus.getTimestamp()).isBeforeOrEqualTo(LocalDateTime.now());
	}
}
