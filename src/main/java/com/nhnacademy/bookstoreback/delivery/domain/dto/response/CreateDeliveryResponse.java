package com.nhnacademy.bookstoreback.delivery.domain.dto.response;

import java.time.LocalDateTime;

import com.nhnacademy.bookstoreback.delivery.domain.entity.Delivery;

import lombok.Builder;

@Builder
public record CreateDeliveryResponse(
	Long deliveryId,
	String deliverySenderName,
	String deliverySenderPhone,
	LocalDateTime deliverySenderDate,
	String deliverySenderAddress,
	String deliveryReceiver,
	String deliveryReceiverPhone,
	LocalDateTime deliveryReceiverDate,
	String deliveryReceiverAddress,
	Long orderId,
	Long deliveryStatusId,
	Long deliveryPolicyId
) {
	public static CreateDeliveryResponse fromEntity(Delivery delivery) {
		return CreateDeliveryResponse.builder()
			.deliveryId(delivery.getDeliveryId())
			.deliverySenderName(delivery.getDeliverySenderName())
			.deliverySenderPhone(delivery.getDeliverySenderPhone())
			.deliverySenderDate(delivery.getDeliverySenderDate())
			.deliverySenderAddress(delivery.getDeliverySenderAddress())
			.deliveryReceiver(delivery.getDeliveryReceiver())
			.deliveryReceiverPhone(delivery.getDeliveryReceiverPhone())
			.deliveryReceiverDate(delivery.getDeliveryReceiverDate())
			.deliveryReceiverAddress(delivery.getDeliveryReceiverAddress())
			.orderId(delivery.getOrder().getOrderId())
			.deliveryStatusId(delivery.getDeliveryStatus().getDeliveryStatusId())
			.deliveryPolicyId(delivery.getDeliveryPolicy().getDeliveryPolicyId())
			.build();
	}
}