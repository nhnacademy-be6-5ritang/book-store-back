package com.nhnacademy.bookstoreback.delivery.domain.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.nhnacademy.bookstoreback.delivery.domain.entity.Delivery;

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
	String deliveryStatusName,
	BigDecimal deliveryPolicyStandardPrice
) {

	public static GetDeliveryResponse fromEntity(Delivery delivery) {
		return GetDeliveryResponse.builder()
			.deliverySenderName(delivery.getDeliverySenderName())
			.deliverySenderPhone(delivery.getDeliverySenderPhone())
			.deliverySenderDate(delivery.getDeliverySenderDate())
			.deliverySenderAddress(delivery.getDeliverySenderAddress())
			.deliveryReceiver(delivery.getDeliveryReceiver())
			.deliveryReceiverPhone(delivery.getDeliveryReceiverPhone())
			.deliveryReceiverDate(delivery.getDeliveryReceiverDate())
			.deliveryReceiverAddress(delivery.getDeliveryReceiverAddress())
			.orderId(delivery.getOrder().getOrderId())
			.deliveryStatusName(delivery.getDeliveryStatus().getDeliveryStatusName())
			.deliveryPolicyStandardPrice(delivery.getDeliveryPolicy().getDeliveryPolicyPrice())
			.build();
	}
}
