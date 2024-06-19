package com.nhnacademy.bookstoreback.address.domain.dto.response;

import com.nhnacademy.bookstoreback.address.domain.entity.Address;

import lombok.Builder;

@Builder
public record RegisterAddressResponse(
	Long id,
	Long userId,
	String postCode,
	String base,
	String detail,
	String alias
) {

	public static RegisterAddressResponse fromEntity(Address address) {
		return RegisterAddressResponse.builder()
			.id(address.getId())
			.userId(address.getUser().getId())
			.postCode(address.getPostCode())
			.base(address.getBase())
			.detail(address.getDetail())
			.alias(address.getAlias())
			.build();
	}
}
