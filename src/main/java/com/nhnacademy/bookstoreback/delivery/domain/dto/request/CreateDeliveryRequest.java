package com.nhnacademy.bookstoreback.delivery.domain.dto.request;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record CreateDeliveryRequest(
	String deliveryReceiver,
	String deliveryReceiverPhone,
	LocalDateTime deliveryReceiverDate,
	String deliveryReceiverAddress,
	String deliveryReceiverAddress2
) {
}
