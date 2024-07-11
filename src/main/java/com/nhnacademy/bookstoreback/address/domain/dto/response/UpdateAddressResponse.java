package com.nhnacademy.bookstoreback.address.domain.dto.response;

import com.nhnacademy.bookstoreback.address.domain.entity.Address;

import lombok.Builder;

@Builder
public record UpdateAddressResponse(
	Long id,
	Long userId,
	String postCode,
	String baseAddress,
	String detailAddress,
	String alias
) {
	public static UpdateAddressResponse fromEntity(Address address) {
		return UpdateAddressResponse.builder()
			.id(address.getId())
			.userId(address.getUser().getId())
			.postCode(address.getPostCode())
			.baseAddress(address.getBase())
			.detailAddress(address.getDetail())
			.alias(address.getAlias())
			.build();
	}
}

