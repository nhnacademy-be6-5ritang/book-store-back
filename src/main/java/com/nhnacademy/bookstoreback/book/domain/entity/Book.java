package com.nhnacademy.bookstoreback.book.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "books")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_id")
	private Long bookId;

	@ManyToOne
	@JoinColumn(name = "author_id", nullable = false)
	private Author author;

	@ManyToOne
	@JoinColumn(name = "publisher_id", nullable = false)
	private Publisher publisher;

	@ManyToOne
	@JoinColumn(name = "book_status_id", nullable = false)
	private BookStatus bookStatus;

	@Column(name = "book_title", nullable = false, length = 100)
	private String bookTitle;

	@Column(name = "book_index", nullable = false)
	private String bookIndex;

	@Column(name = "book_description", nullable = false)
	private String bookDescription;

	@Column(name = "book_packaging", nullable = false)
	private boolean bookPackaging = false;

	@Column(name = "book_publish_date", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date bookPublishDate;

	@Column(name = "book_isbn", nullable = false, length = 17)
	private String bookIsbn;

	@Column(name = "book_price", nullable = false)
	private BigDecimal bookPrice;

	@Column(name = "book_sale_percent", nullable = false)
	private BigDecimal bookSalePercent;

	@Column(name = "book_sale_price", nullable = false)
	private BigDecimal bookSalePrice;
}