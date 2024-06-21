package com.nhnacademy.bookstoreback.deliverystatus.domain.dto.response;

import lombok.Builder;

@Builder
public record UpdateDeliveryStatusResponse(Long deliveryStatusId, String deliveryStatusName) {
}
