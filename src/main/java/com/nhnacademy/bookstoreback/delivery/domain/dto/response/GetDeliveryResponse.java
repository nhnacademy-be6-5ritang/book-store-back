package com.nhnacademy.bookstoreback.delivery.domain.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record GetDeliveryResponse(
	String deliverySenderName,
	String deliverySenderPhone,
	LocalDateTime deliverySenderDate,
	String deliverySenderAddress,
	String deliveryReceiver,
	String deliveryReceiverPhone,
	LocalDateTime deliveryReceiverDate,
	String deliveryReceiverAddress,
	Long orderId,
	String deliveryStatusName
) {
}
