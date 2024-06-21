package com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record GetDeliveryPolicyResponse(String deliveryPolicyName,
										BigDecimal deliveryPolicyPrice, String deliveryPolicyContent) {
}
