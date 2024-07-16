package com.nhnacademy.bookstoreback.book.domain.dto.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * 책 상세페이지 응답 DTO
 *
 * @author 김기욱
 * @version 1.0
 */
@Builder
public record CreateBookRequest(
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
	@NotNull BigDecimal bookSalePercent) {

	public static CreateBookRequest fromEntity(Book book) {
		return CreateBookRequest.builder()
			.bookIsbn(book.getBookIsbn())
			.bookTitle(book.getBookTitle())
			.authorName(book.getAuthor().getAuthorName())
			.publisherName(book.getPublisher().getPublisherName())
			.bookPublishDate(book.getBookPublishDate())
			.bookStatusName(book.getBookStatus().getBookStatusName())
			.bookDescription(book.getBookDescription())
			.bookQuantity(book.getBookQuantity())
			.bookPrice(book.getBookPrice())
			.bookSalePrice(book.getBookSalePrice())
			.bookSalePercent(book.getBookSalePercent())
			.build();
	}
}
