package com.nhnacademy.bookstoreback.publisher.exception;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

class PublisherExceptionTest {

	@Test
	void testPublisherAlreadyExistsException() {
		String publisherName = "existing-publisher";
		PublisherAlreadyExistsException exception = new PublisherAlreadyExistsException(publisherName);

		assertThat(exception).isInstanceOf(AlreadyExistsException.class);
		ErrorStatus errorStatus = exception.getErrorStatus();
		assertThat(errorStatus).isNotNull();
		assertThat(errorStatus.getMessage()).isEqualTo(String.format("해당 출판사 '%s'는 이미 존재 하는 출판사 입니다.", publisherName));
		assertThat(errorStatus.getStatus()).isEqualTo(HttpStatus.CONFLICT);
		assertThat(errorStatus.getTimestamp()).isBeforeOrEqualTo(LocalDateTime.now());
	}

	@Test
	void testPublisherNotFoundException() {
		String publisherName = "non-existing-publisher";
		PublisherNotFoundException exception = new PublisherNotFoundException(publisherName);

		assertThat(exception).isInstanceOf(NotFoundException.class);
		ErrorStatus errorStatus = exception.getErrorStatus();
		assertThat(errorStatus).isNotNull();
		assertThat(errorStatus.getMessage()).isEqualTo(String.format("해당 출판사 '%s'는 존재하지 않는 출판사 입니다.", publisherName));
		assertThat(errorStatus.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(errorStatus.getTimestamp()).isBeforeOrEqualTo(LocalDateTime.now());
	}
}