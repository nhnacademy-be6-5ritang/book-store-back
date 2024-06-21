package com.nhnacademy.bookstoreback.deliverystatus.domain.dto.response;

import lombok.Builder;

@Builder
public record CreateDeliveryStatusResponse(Long deliveryStatusId, String deliveryStatusName) {
}
