package com.nhnacademy.bookstoreback.address.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RegisterAddressRequest(
	@NotBlank @Size(max = 30)
	String alias,
	@NotBlank @Size(min = 5, max = 5)
	String postCode,
	@NotBlank @Size(max = 50)
	String baseAddress,
	@NotBlank @Size(max = 30)
	String detailAddress
) {
}
