package com.nhnacademy.bookstoreback.delivery.domain.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateDeliveryRequest(
	@NotBlank @Size(max = 20) String deliveryReceiver,
	@NotBlank @Size(max = 20) String deliveryReceiverPhone,
	@NotNull LocalDateTime deliveryReceiverDate,
	@NotBlank @Size(max = 40) String deliveryReceiverAddress,
	@NotBlank @Size(max = 60) String deliveryReceiverAddress2
) {
}
