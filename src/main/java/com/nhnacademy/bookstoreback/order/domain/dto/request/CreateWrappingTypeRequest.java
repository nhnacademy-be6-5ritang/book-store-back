package com.nhnacademy.bookstoreback.order.domain.dto.request;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record CreateWrappingTypeRequest(
	String paperName,
	String paperContent,
	BigDecimal paperPrice
) {

}
