package com.nhnacademy.bookstoreback.address.domain.dto.request;

import lombok.Builder;

@Builder
public record RegisterAddressRequest(
	String alias,
	String postCode,
	String baseAddress,
	String detailAddress
) {
}
