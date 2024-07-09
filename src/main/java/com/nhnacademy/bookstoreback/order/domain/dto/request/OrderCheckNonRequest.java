package com.nhnacademy.bookstoreback.order.domain.dto.request;

public record OrderCheckNonRequest(
	String payerEmail,
	String orderInfoId
) {
}
