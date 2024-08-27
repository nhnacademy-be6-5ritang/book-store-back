package com.nhnacademy.bookstoreback.book.domain.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * @author 이경헌
 * 책 간략 페이지 응답 DTO
 */
@Builder
public record GetBookSimpleResponse(
        Long bookId,
        String authorName,
        String bookTitle,
        BigDecimal bookPrice,
        BigDecimal bookSalePrice,
        BigDecimal bookSalePercent,
        String bookImageUrl) {
}
