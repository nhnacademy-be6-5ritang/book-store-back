package com.nhnacademy.bookstoreback.delivery.domain.dto.response;

import com.nhnacademy.bookstoreback.delivery.domain.entity.Delivery;

import lombok.Builder;

@Builder
public record UpdateDeliveryAddOrderPolicyResponse(
	Long deliveryId) {

	public static UpdateDeliveryAddOrderPolicyResponse fromEntity(Delivery delivery) {
		return UpdateDeliveryAddOrderPolicyResponse.builder()
			.deliveryId(delivery.getDeliveryId())
			.build();
	}
}
