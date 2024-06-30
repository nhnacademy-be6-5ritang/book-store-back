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
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Book Entity
 *
 * @author 김기욱
 * @version 1.0
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@Table(name = "books")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_id")
	private Long bookId;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "author_id")
	private Author author;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "publisher_id")
	private Publisher publisher;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "book_status_id")
	private BookStatus bookStatus;

	@NotNull
	@Column(name = "book_title", length = 300)
	private String bookTitle;

	@Column(name = "book_index")
	private String bookIndex = "책 목차";

	@NotNull
	@Column(name = "book_description")
	private String bookDescription;

	@NotNull
	@Column(name = "book_packaging")
	private boolean bookPackaging = false;

	@NotNull
	@Column(name = "book_quantity")
	private int bookQuantity = 100;

	@NotNull
	@Column(name = "book_publish_date")
	@Temporal(TemporalType.DATE)
	private Date bookPublishDate;

	@NotNull
	@Column(name = "book_isbn", length = 17)
	private String bookIsbn;

	@NotNull
	@Column(name = "book_price")
	private BigDecimal bookPrice;

	@NotNull
	@Column(name = "book_sale_percent")
	private BigDecimal bookSalePercent;

	@NotNull
	@Column(name = "book_sale_price")
	private BigDecimal bookSalePrice;

	public void update(boolean result) {
		this.bookPackaging = result;
	}
}