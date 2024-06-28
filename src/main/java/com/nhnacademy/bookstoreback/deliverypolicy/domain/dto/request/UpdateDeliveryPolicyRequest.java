package com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.request;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record UpdateDeliveryPolicyRequest(
	String deliveryPolicyName,
	BigDecimal deliveryPolicyPrice,
	String deliveryPolicyContent,
	BigDecimal deliveryPolicyStandardPrice) {
}
