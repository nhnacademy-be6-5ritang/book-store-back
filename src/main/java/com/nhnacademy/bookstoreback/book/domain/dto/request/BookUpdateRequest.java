package com.nhnacademy.bookstoreback.book.domain.dto.request;

import java.math.BigDecimal;

import lombok.Builder;

/**
 * 책 수정 요청 DTO
 *
 * @version 1.0
 */
@Builder
public record BookUpdateRequest(String statusName, String description, String index, int quantity, BigDecimal price,
								BigDecimal salePrice) {
}
