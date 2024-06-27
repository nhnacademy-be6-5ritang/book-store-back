package com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response;

import com.nhnacademy.bookstoreback.deliverypolicy.domain.entity.DeliveryPolicy;

import lombok.Builder;

@Builder
public record GetDeliveryPoliciesResponse(
	Long deliveryPolicyId,
	String deliveryPolicyName) {

	public static GetDeliveryPoliciesResponse fromEntity(DeliveryPolicy deliveryPolicy) {
		return GetDeliveryPoliciesResponse.builder()
			.deliveryPolicyId(deliveryPolicy.getDeliveryPolicyId())
			.deliveryPolicyName(deliveryPolicy.getDeliveryPolicyName())
			.build();
	}
}
