package com.nhnacademy.bookstoreback.order.domain.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateWrappingTypeRequest(
	@NotBlank @Size(max = 20) String paperName,
	@NotBlank @Size(max = 200) String paperContent,
	@NotNull BigDecimal paperPrice
) {

}
