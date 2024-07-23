package com.nhnacademy.bookstoreback.deliverystatus.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

class DeliveryStatusExceptionTest {

	@Test
	void testDeliveryStatusAlreadyExistsException() {
		String value = "Shipped";

		DeliveryStatusAlreadyExistsException exception = assertThrows(DeliveryStatusAlreadyExistsException.class,
			() -> {
				throw new DeliveryStatusAlreadyExistsException(value);
			});

		ErrorStatus errorStatus = exception.getErrorStatus();
		assertEquals(String.format("해당 배송상태 '%s'는 이미 존재 하는 배송상태 입니다.", value), errorStatus.getMessage());
		assertEquals(HttpStatus.CONFLICT, errorStatus.getStatus());
		assertEquals(LocalDateTime.now().getDayOfYear(),
			errorStatus.getTimestamp().getDayOfYear());
	}

	@Test
	void testDeliveryStatusNotFoundException() {
		String value = "1";

		DeliveryStatusNotFoundException exception = assertThrows(DeliveryStatusNotFoundException.class, () -> {
			throw new DeliveryStatusNotFoundException(value);
		});

		ErrorStatus errorStatus = exception.getErrorStatus();
		assertEquals(String.format("해당 배송상태 '%s'는 존재하지 않는 배송상태 입니다.", value), errorStatus.getMessage());
		assertEquals(HttpStatus.NOT_FOUND, errorStatus.getStatus());
		assertEquals(LocalDateTime.now().getDayOfYear(),
			errorStatus.getTimestamp().getDayOfYear()); // Only comparing the day to avoid millisecond differences
	}
}
