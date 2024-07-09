package com.nhnacademy.bookstoreback.delivery.domain.dto.request;

import lombok.Builder;

@Builder
public record UpdateDeliveryByOrderIdRequest(
	String senderName,
	String senderAddress,
	String senderAddress2,
	String senderPhone
) {
}