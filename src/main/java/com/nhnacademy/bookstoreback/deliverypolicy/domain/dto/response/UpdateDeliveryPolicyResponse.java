package com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.deliverypolicy.domain.entity.DeliveryPolicy;

import lombok.Builder;

@Builder
public record UpdateDeliveryPolicyResponse(
	Long deliveryPolicyId,
	String deliveryPolicyName,
	BigDecimal deliveryPolicyPrice,
	String deliveryPolicyContent,
	BigDecimal deliveryPolicyStandardPrice) {

	public static UpdateDeliveryPolicyResponse fromEntity(DeliveryPolicy deliveryPolicy) {
		return UpdateDeliveryPolicyResponse.builder()
			.deliveryPolicyId(deliveryPolicy.getDeliveryPolicyId())
			.deliveryPolicyName(deliveryPolicy.getDeliveryPolicyName())
			.deliveryPolicyPrice(deliveryPolicy.getDeliveryPolicyPrice())
			.deliveryPolicyContent(deliveryPolicy.getDeliveryPolicyContent())
			.build();
	}
}
