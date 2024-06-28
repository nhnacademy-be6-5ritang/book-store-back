package com.nhnacademy.bookstoreback.delivery.domain.dto.response;

import com.nhnacademy.bookstoreback.delivery.domain.entity.Delivery;

import lombok.Builder;

@Builder
public record UpdateDeliveryResponse(
	Long deliveryId,
	String deliveryStatusName) {

	public static UpdateDeliveryResponse fromEntity(Delivery delivery) {
		return UpdateDeliveryResponse.builder()
			.deliveryId(delivery.getDeliveryId())
			.deliveryStatusName(delivery.getDeliveryStatus().getDeliveryStatusName())
			.build();
	}
}
