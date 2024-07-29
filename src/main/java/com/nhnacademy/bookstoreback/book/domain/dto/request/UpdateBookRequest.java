package com.nhnacademy.bookstoreback.book.domain.dto.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 책 상세페이지 응답 DTO
 *
 * @author 김기욱
 * @version 1.0
 */
public record UpdateBookRequest(
	@NotBlank @Size(max = 17) String bookIsbn,
	@NotNull List<Long> categories,
	List<Long> tags,
	@NotBlank @Size(max = 300) String bookTitle,
	@NotBlank @Size(max = 200) String authorName,
	@NotBlank @Size(max = 100) String publisherName,
	@NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") Date bookPublishDate,
	@NotBlank @Size(max = 10) String bookStatusName,
	@NotBlank String bookDescription,
	@NotNull int bookQuantity,
	@NotNull BigDecimal bookPrice,
	@NotNull BigDecimal bookSalePrice,
	@NotNull BigDecimal bookSalePercent,
	String fileName) {
}
