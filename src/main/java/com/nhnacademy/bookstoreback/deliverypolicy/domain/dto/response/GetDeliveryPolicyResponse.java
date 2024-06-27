package com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.deliverypolicy.domain.entity.DeliveryPolicy;

import lombok.Builder;

@Builder
public record GetDeliveryPolicyResponse(
	Long deliveryPolicyId,
	String deliveryPolicyName,
	BigDecimal deliveryPolicyPrice,
	String deliveryPolicyContent) {

	public static GetDeliveryPolicyResponse fromEntity(DeliveryPolicy deliveryPolicy) {
		return GetDeliveryPolicyResponse.builder()
			.deliveryPolicyId(deliveryPolicy.getDeliveryPolicyId())
			.deliveryPolicyName(deliveryPolicy.getDeliveryPolicyName())
			.deliveryPolicyPrice(deliveryPolicy.getDeliveryPolicyPrice())
			.deliveryPolicyContent(deliveryPolicy.getDeliveryPolicyContent())
			.build();
	}
}
