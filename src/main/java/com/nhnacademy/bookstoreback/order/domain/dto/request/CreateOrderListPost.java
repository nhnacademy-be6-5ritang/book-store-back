package com.nhnacademy.bookstoreback.order.domain.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateOrderListPost(
	@NotNull List<Long> paperId,
	List<Integer> quantity
) {
}
