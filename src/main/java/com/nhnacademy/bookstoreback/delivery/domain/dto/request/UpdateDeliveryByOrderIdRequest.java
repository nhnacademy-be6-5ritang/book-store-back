package com.nhnacademy.bookstoreback.delivery.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateDeliveryByOrderIdRequest(
	@NotBlank @Size(max = 20) String senderName,
	@NotBlank @Size(max = 40) String senderAddress,
	@NotBlank @Size(max = 60) String senderAddress2,
	@NotBlank @Size(max = 20) String senderPhone
) {
}