package com.nhnacademy.bookstoreback.deliverypolicy.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

class DeliveryPolicyExceptionTest {

	@Test
	void testDeliveryPolicyAlreadyExistsException() {
		String policyName = "Standard Delivery";
		DeliveryPolicyAlreadyExistsException exception = new DeliveryPolicyAlreadyExistsException(policyName);

		ErrorStatus errorStatus = exception.getErrorStatus();

		assertNotNull(exception);
		assertNotNull(errorStatus);
		assertEquals("해당 배송비정책 'Standard Delivery'는 이미 존재 하는 배송비정책 입니다.", errorStatus.getMessage());
		assertEquals(HttpStatus.CONFLICT, errorStatus.getStatus());
	}

	@Test
	void testDeliveryPolicyNotFoundException() {
		Long policyId = 123L;
		DeliveryPolicyNotFoundException exception = new DeliveryPolicyNotFoundException(policyId);

		ErrorStatus errorStatus = exception.getErrorStatus();
		
		assertNotNull(exception);
		assertNotNull(errorStatus);
		assertEquals("해당 배송비정책 '123'는 존재하지 않는 배송비정책 입니다.", errorStatus.getMessage());
		assertEquals(HttpStatus.NOT_FOUND, errorStatus.getStatus());
	}
}
