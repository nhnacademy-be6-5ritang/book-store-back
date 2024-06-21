package com.nhnacademy.bookstoreback.deliverystatus.domain.dto.request;

import lombok.Builder;

@Builder
public record UpdateDeliveryStatusRequest(String deliveryStatusName) {
}
