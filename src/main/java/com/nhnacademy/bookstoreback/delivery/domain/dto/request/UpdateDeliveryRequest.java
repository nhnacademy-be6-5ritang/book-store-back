package com.nhnacademy.bookstoreback.delivery.domain.dto.request;

import lombok.Builder;

@Builder
public record UpdateDeliveryRequest(Long deliveryStatusId) {
}