package com.nhnacademy.bookstoreback.deliverystatus.domain.dto.response;

import lombok.Builder;

@Builder
public record GetDeliveryStatusResponse(String deliveryStatusName) {
}
