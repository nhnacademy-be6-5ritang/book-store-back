package com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.deliverypolicy.domain.entity.DeliveryPolicy;

import lombok.Builder;

@Builder
public record CreateDeliveryPolicyResponse(
	Long deliveryPolicyId,
	String deliveryPolicyName,
	BigDecimal deliveryPolicyPrice,
	String deliveryPolicyContent,
	BigDecimal deliveryPolicyStandardPrice) {

	public static CreateDeliveryPolicyResponse fromEntity(DeliveryPolicy deliveryPolicy) {
		return CreateDeliveryPolicyResponse.builder()
			.deliveryPolicyId(deliveryPolicy.getDeliveryPolicyId())
			.deliveryPolicyName(deliveryPolicy.getDeliveryPolicyName())
			.deliveryPolicyContent(deliveryPolicy.getDeliveryPolicyContent())
			.deliveryPolicyPrice(deliveryPolicy.getDeliveryPolicyPrice())
			.deliveryPolicyStandardPrice(deliveryPolicy.getDeliveryPolicyStandardPrice())
			.build();
	}
}
