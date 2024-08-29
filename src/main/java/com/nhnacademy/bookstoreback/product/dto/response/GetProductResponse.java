package com.nhnacademy.bookstoreback.product.dto.response;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author 이경헌
 * 상품 페이지 응답 DTO
 */
public record GetProductResponse(
	Long bookId,
	String authorName,
	String publisherName,
	String bookStatusName,
	String bookTitle,
	String bookDescription,
	int bookQuantity,
	@DateTimeFormat(pattern = "yyyy-MM-dd") Date bookPublishDate,
	String bookIsbn,
	BigDecimal bookPrice,
	BigDecimal bookSalePrice,
	BigDecimal bookSalePercent,
	String bookImageUrl,
	List<String> bookCategories,
	List<String> bookTags,
	double reviewScoreAvg) {
}
