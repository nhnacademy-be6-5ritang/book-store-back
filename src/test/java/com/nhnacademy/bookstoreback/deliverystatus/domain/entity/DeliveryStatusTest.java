package com.nhnacademy.bookstoreback.deliverystatus.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class DeliveryStatusTest {

	private Validator validator;

	@BeforeEach
	void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	void testValidDeliveryStatus() {
		DeliveryStatus deliveryStatus = new DeliveryStatus("Shipped");

		Set<ConstraintViolation<DeliveryStatus>> violations = validator.validate(deliveryStatus);
		assertEquals(0, violations.size());
	}

	@Test
	void testInvalidDeliveryStatusNameBlank() {
		DeliveryStatus deliveryStatus = new DeliveryStatus("");

		Set<ConstraintViolation<DeliveryStatus>> violations = validator.validate(deliveryStatus);
		assertEquals(1, violations.size());

		ConstraintViolation<DeliveryStatus> violation = violations.iterator().next();
		assertEquals("공백일 수 없습니다", violation.getMessage());
	}

	@Test
	void testInvalidDeliveryStatusNameSize() {
		DeliveryStatus deliveryStatus = new DeliveryStatus("ThisNameIsTooLong");

		Set<ConstraintViolation<DeliveryStatus>> violations = validator.validate(deliveryStatus);
		assertEquals(1, violations.size());

		ConstraintViolation<DeliveryStatus> violation = violations.iterator().next();
		assertEquals("크기가 0에서 10 사이여야 합니다", violation.getMessage());
	}

	@Test
	void testUpdateDeliveryStatus() {
		DeliveryStatus deliveryStatus = new DeliveryStatus("Shipped");
		deliveryStatus.updateDeliveryStatus("Delivered");

		assertEquals("Delivered", deliveryStatus.getDeliveryStatusName());
	}

	@Test
	void testConstructorAndGetter() {
		DeliveryStatus deliveryStatus = new DeliveryStatus("In Transit");

		assertEquals("In Transit", deliveryStatus.getDeliveryStatusName());
	}

	@Test
	void testProtectedNoArgsConstructor() {
		DeliveryStatus deliveryStatus = new DeliveryStatus();

		assertEquals(null, deliveryStatus.getDeliveryStatusId());
		assertEquals(null, deliveryStatus.getDeliveryStatusName());
	}
}