package com.nhnacademy.bookstoreback.address.domain.dto.request;

public record UpdateAddressRequest(
	String alias,
	String postCode,
	String baseAddress,
	String detailAddress
) {
}
