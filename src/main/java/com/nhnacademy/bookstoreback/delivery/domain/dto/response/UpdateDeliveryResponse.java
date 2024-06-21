package com.nhnacademy.bookstoreback.delivery.domain.dto.response;

import lombok.Builder;

@Builder
public record UpdateDeliveryResponse(Long deliveryId, Long orderId, String deliveryStatusName) {
}
