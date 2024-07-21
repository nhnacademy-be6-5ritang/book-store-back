package com.nhnacademy.bookstoreback.deliverystatus.domain.dto.request;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class DeliveryStatusRequestTest {

	private static Validator validator;

	@BeforeAll
	static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@AfterAll
	static void tearDown() {
		validator = null;
	}

	@Test
	void testCreateDeliveryStatusRequestInvalid() {
		CreateDeliveryStatusRequest request = new CreateDeliveryStatusRequest("");
		Set<ConstraintViolation<CreateDeliveryStatusRequest>> violations = validator.validate(request);
		assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("deliveryStatusName")));
	}

	@Test
	void testUpdateDeliveryStatusRequestInvalid() {
		UpdateDeliveryStatusRequest request = new UpdateDeliveryStatusRequest("");
		Set<ConstraintViolation<UpdateDeliveryStatusRequest>> violations = validator.validate(request);
		assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("deliveryStatusName")));
	}

	@Test
	void testCreateDeliveryStatusRequestValid() {
		CreateDeliveryStatusRequest request = new CreateDeliveryStatusRequest("Delivered");
		Set<ConstraintViolation<CreateDeliveryStatusRequest>> violations = validator.validate(request);
		assertTrue(violations.isEmpty());
	}

	@Test
	void testUpdateDeliveryStatusRequestValid() {
		UpdateDeliveryStatusRequest request = new UpdateDeliveryStatusRequest("Shipped");
		Set<ConstraintViolation<UpdateDeliveryStatusRequest>> violations = validator.validate(request);
		assertTrue(violations.isEmpty());
	}
}
