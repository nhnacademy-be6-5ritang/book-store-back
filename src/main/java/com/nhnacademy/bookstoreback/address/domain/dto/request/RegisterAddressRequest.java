package com.nhnacademy.bookstoreback.address.domain.dto.request;

import lombok.Builder;

@Builder
public record RegisterAddressRequest(
	Long userId,
	String postCode,
	String base,
	String detail,
	String alias
) {
}
