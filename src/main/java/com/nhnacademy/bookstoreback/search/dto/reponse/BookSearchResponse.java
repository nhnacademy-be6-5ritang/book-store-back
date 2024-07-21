package com.nhnacademy.bookstoreback.search.dto.reponse;

import java.math.BigDecimal;
import java.util.Date;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;

import lombok.Builder;
import lombok.Data;

/**
 * 책 상세페이지 응답 DTO
 */
@Data
@Builder
public class BookSearchResponse {
	private Long bookId;
	private String authorName;
	private String publisherName;
	private String bookStatusName;
	private String bookTitle;
	private String bookDescription;
	private int bookQuantity;
	private Date bookPublishDate;
	private String bookIsbn;
	private BigDecimal bookPrice;
	private BigDecimal bookSalePrice;
	private BigDecimal bookSalePercent;
	private String bookImageUrl;

	/**
	 * Book 엔티티를 BookDetailResponse DTO로 변환하는 메소드
	 *
	 * @param book Book 엔티티
	 * @return BookDetailResponse DTO
	 */
	public static BookSearchResponse fromEntity(Book book) {
		String imageUrl = book.getBookImages().stream()
			.map(bookImage -> bookImage.getImage().getImageUrl())
			.findFirst()
			.orElse(null); // 이미지가 없는 경우 null 반환

		return BookSearchResponse.builder()
			.bookId(book.getBookId())
			.authorName(book.getAuthor().getAuthorName())
			.publisherName(book.getPublisher().getPublisherName())
			.bookStatusName(book.getBookStatus().getBookStatusName())
			.bookTitle(book.getBookTitle())
			.bookDescription(book.getBookDescription())
			.bookQuantity(book.getBookQuantity())
			.bookPublishDate(book.getBookPublishDate())
			.bookIsbn(book.getBookIsbn())
			.bookPrice(book.getBookPrice())
			.bookSalePrice(book.getBookSalePrice())
			.bookSalePercent(book.getBookSalePercent())
			.bookImageUrl(imageUrl)
			.build();
	}
}
