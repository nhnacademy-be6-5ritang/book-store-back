package com.nhnacademy.bookstoreback.book.domain.dto.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BookRequest {
	private String bookTitle;
	private String bookIndex;
	private String bookDescription;
	private String bookIsbn;
	private BigDecimal bookPrice;
	private BigDecimal bookSalePercent;
	private BigDecimal bookSalePrice;
}