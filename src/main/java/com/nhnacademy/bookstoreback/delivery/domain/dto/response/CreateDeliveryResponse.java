package com.nhnacademy.bookstoreback.delivery.domain.dto.response;

import java.time.LocalDateTime;

import com.nhnacademy.bookstoreback.delivery.domain.entity.Delivery;

import lombok.Builder;

@Builder
public record CreateDeliveryResponse(
	Long deliveryId,
	String deliveryReceiver,
	String deliveryReceiverPhone,
	LocalDateTime deliveryReceiverDate,
	String deliveryReceiverAddress,
	Long deliveryStatusId
) {
	public static CreateDeliveryResponse fromEntity(Delivery delivery) {
		return CreateDeliveryResponse.builder()
			.deliveryId(delivery.getDeliveryId())
			.deliveryReceiver(delivery.getDeliveryReceiver())
			.deliveryReceiverPhone(delivery.getDeliveryReceiverPhone())
			.deliveryReceiverDate(delivery.getDeliveryReceiverDate())
			.deliveryReceiverAddress(delivery.getDeliveryReceiverAddress())
			.deliveryStatusId(delivery.getDeliveryStatus().getDeliveryStatusId())
			.build();
	}
}
