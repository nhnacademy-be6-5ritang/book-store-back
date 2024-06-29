package com.nhnacademy.bookstoreback.book.domain.dto.response;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Builder;

/**
 * 책 상세페이지 응답 DTO
 *
 * @author 김기욱
 * @version 1.0
 */
@Builder
public record BookDetailResponse(String authorName, String publisherName, String bookStatus, String bookTitle,
								 String bookDescription, String bookIndex, boolean bookPackaging, int bookQuantity,
								 Date bookPublishDate, String bookIsbn, BigDecimal bookPrice, BigDecimal bookSalePrice,
								 BigDecimal bookSalePercent) {
}