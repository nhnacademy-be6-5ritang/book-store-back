package com.nhnacademy.bookstoreback.delivery.domain.dto.request;

import lombok.Builder;

@Builder
public record UpdateDeliveryAddOrderPolicyRequest(
	Long deliveryId,
	Long orderId,
	Long deliveryPolicyId
) {
}
