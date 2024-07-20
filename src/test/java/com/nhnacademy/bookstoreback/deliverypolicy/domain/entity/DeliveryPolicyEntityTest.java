package com.nhnacademy.bookstoreback.deliverypolicy.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.request.CreateDeliveryPolicyRequest;

class DeliveryPolicyEntityTest {
	@Test
	void testNoArgsConstructorIsProtected() {
		try {
			Constructor<DeliveryPolicy> constructor = DeliveryPolicy.class.getDeclaredConstructor();
			assertTrue(java.lang.reflect.Modifier.isProtected(constructor.getModifiers()),
				"Default constructor is not protected");
		} catch (NoSuchMethodException e) {
			fail("No default constructor found");
		}
		new DeliveryPolicy();
	}

	@Test
	void testDeliveryPolicyBuilder() {
		String policyName = "Standard Delivery";
		BigDecimal policyPrice = new BigDecimal("1500.00");
		String policyContent = "Standard delivery policy description.";
		BigDecimal standardPrice = new BigDecimal("5000.00");

		DeliveryPolicy deliveryPolicy = DeliveryPolicy.builder()
			.deliveryPolicyName(policyName)
			.deliveryPolicyPrice(policyPrice)
			.deliveryPolicyContent(policyContent)
			.deliveryPolicyStandardPrice(standardPrice)
			.build();

		assertNull(deliveryPolicy.getDeliveryPolicyId());
		assertNotNull(deliveryPolicy);
		assertEquals(policyName, deliveryPolicy.getDeliveryPolicyName());
		assertEquals(policyPrice, deliveryPolicy.getDeliveryPolicyPrice());
		assertEquals(policyContent, deliveryPolicy.getDeliveryPolicyContent());
		assertEquals(standardPrice, deliveryPolicy.getDeliveryPolicyStandardPrice());
	}

	@Test
	void testToEntity() {
		CreateDeliveryPolicyRequest request = new CreateDeliveryPolicyRequest(
			"Express Delivery", "Express delivery policy description.", new BigDecimal("2500.00"),
			new BigDecimal("10000.00"));

		DeliveryPolicy deliveryPolicy = DeliveryPolicy.toEntity(request);

		assertNotNull(deliveryPolicy);
		assertEquals(request.deliveryPolicyName(), deliveryPolicy.getDeliveryPolicyName());
		assertEquals(request.deliveryPolicyPrice(), deliveryPolicy.getDeliveryPolicyPrice());
		assertEquals(request.deliveryPolicyContent(), deliveryPolicy.getDeliveryPolicyContent());
		assertEquals(request.deliveryPolicyStandardPrice(), deliveryPolicy.getDeliveryPolicyStandardPrice());
	}

	@Test
	void testUpdateDeliveryPolicy() {
		DeliveryPolicy deliveryPolicy = DeliveryPolicy.builder()
			.deliveryPolicyName("Standard Delivery")
			.deliveryPolicyPrice(new BigDecimal("1500.00"))
			.deliveryPolicyContent("Standard delivery policy description.")
			.deliveryPolicyStandardPrice(new BigDecimal("5000.00"))
			.build();

		String newPolicyName = "Premium Delivery";
		BigDecimal newPolicyPrice = new BigDecimal("3000.00");
		String newPolicyContent = "Updated delivery policy description.";
		BigDecimal newStandardPrice = new BigDecimal("7000.00");

		deliveryPolicy.updateDeliveryPolicy(newPolicyName, newPolicyPrice, newPolicyContent, newStandardPrice);

		assertEquals(newPolicyName, deliveryPolicy.getDeliveryPolicyName());
		assertEquals(newPolicyPrice, deliveryPolicy.getDeliveryPolicyPrice());
		assertEquals(newPolicyContent, deliveryPolicy.getDeliveryPolicyContent());
		assertEquals(newStandardPrice, deliveryPolicy.getDeliveryPolicyStandardPrice());
	}
}