package com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.request;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record CreateDeliveryPolicyRequest(String deliveryPolicyName,
										  BigDecimal deliveryPolicyPrice, String deliveryPolicyContent) {
}
