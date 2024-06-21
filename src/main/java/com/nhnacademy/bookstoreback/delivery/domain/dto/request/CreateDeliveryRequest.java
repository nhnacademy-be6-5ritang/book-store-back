package com.nhnacademy.bookstoreback.delivery.domain.dto.request;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record CreateDeliveryRequest(
	String deliverySenderName,
	String deliverySenderPhone,
	LocalDateTime deliverySenderDate,
	String deliverySenderAddress,
	String deliveryReceiver,
	String deliveryReceiverPhone,
	LocalDateTime deliveryReceiverDate,
	String deliveryReceiverAddress,
	Long orderId
) {
}