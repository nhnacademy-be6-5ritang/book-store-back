package com.nhnacademy.bookstoreback.delivery.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateDeliveryRequest(@NotNull Long deliveryStatusId) {
}
