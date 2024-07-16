package com.nhnacademy.bookstoreback.deliverystatus.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateDeliveryStatusRequest(
	@NotBlank @Size(max = 10) String deliveryStatusName) {
}
