package com.nhnacademy.bookstoreback.product.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * @author 이경헌
 * 상품 리스트 응답 DTO
 */
@Builder
public record GetProductSimpleResponse(
        Long bookId,
        String authorName,
        String bookTitle,
        BigDecimal bookPrice,
        BigDecimal bookSalePrice,
        BigDecimal bookSalePercent,
        String bookImageUrl) {
}
