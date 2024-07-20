package com.nhnacademy.bookstoreback.delivery.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateDeliveryAddOrderPolicyRequest(
	@NotNull Long deliveryId,
	@NotNull Long orderId,
	@NotNull Long deliveryPolicyId
) {
}
