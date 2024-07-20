package com.nhnacademy.bookstoreback.order.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateOrderStatusRequest(
	@NotBlank @Size(max = 10) String orderStatusName
) {
}
