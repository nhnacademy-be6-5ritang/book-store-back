package com.nhnacademy.bookstoreback.address.domain.dto.response;

import com.nhnacademy.bookstoreback.address.domain.entity.Address;

import lombok.Builder;

@Builder
public record GetAddressResponse(
	Long id,
	String postCode,
	String baseAddress,
	String detailAddress,
	String alias,
	boolean isDefault
) {

	public static GetAddressResponse fromEntity(Address address) {
		return GetAddressResponse.builder()
			.id(address.getId())
			.postCode(address.getPostCode())
			.baseAddress(address.getBase())
			.detailAddress(address.getDetail())
			.alias(address.getAlias())
			.isDefault(address.getIsDefault())
			.build();
	}
}
