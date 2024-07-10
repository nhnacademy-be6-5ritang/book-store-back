package com.nhnacademy.bookstoreback.book.domain.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.nhnacademy.bookstoreback.author.domain.entity.Author;
import com.nhnacademy.bookstoreback.book.domain.dto.request.CreateBookRequest;
import com.nhnacademy.bookstoreback.bookstatus.domain.entity.BookStatus;
import com.nhnacademy.bookstoreback.category.domain.entity.BookCategory;
import com.nhnacademy.bookstoreback.publisher.domain.entity.Publisher;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

	@NotNull
	@Column(name = "book_description")
	private String bookDescription;

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

	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<BookImage> bookImages;

	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<BookCategory> bookCategories;

	@Builder
	public Book(
		Author author,
		Publisher publisher,
		BookStatus bookStatus,
		String bookTitle,
		String bookDescription,
		int bookQuantity,
		Date bookPublishDate,
		String bookIsbn,
		BigDecimal bookPrice,
		BigDecimal bookSalePercent,
		BigDecimal bookSalePrice) {
		this.author = author;
		this.publisher = publisher;
		this.bookStatus = bookStatus;
		this.bookTitle = bookTitle;
		this.bookDescription = bookDescription;
		this.bookQuantity = bookQuantity;
		this.bookPublishDate = bookPublishDate;
		this.bookIsbn = bookIsbn;
		this.bookPrice = bookPrice;
		this.bookSalePercent = bookSalePercent;
		this.bookSalePrice = bookSalePrice;
	}

	public static Book toEntity(CreateBookRequest request, Author author, Publisher publisher, BookStatus bookStatus) {
		return Book.builder()
			.author(author)
			.publisher(publisher)
			.bookStatus(bookStatus)
			.bookTitle(request.bookTitle())
			.bookDescription(request.bookDescription())
			.bookQuantity(request.bookQuantity())
			.bookPublishDate(request.bookPublishDate())
			.bookIsbn(request.bookIsbn())
			.bookPrice(request.bookPrice())
			.bookSalePercent(request.bookSalePercent())
			.bookSalePrice(request.bookSalePrice())
			.build();
	}

	public void updateBook(Author author, Publisher publisher, BookStatus bookStatus, String bookTitle,
		String bookDescription, int bookQuantity, Date bookPublishDate,
		String bookIsbn, BigDecimal bookPrice, BigDecimal bookSalePercent, BigDecimal bookSalePrice) {
		this.author = author;
		this.publisher = publisher;
		this.bookStatus = bookStatus;
		this.bookTitle = bookTitle;
		this.bookDescription = bookDescription;
		this.bookQuantity = bookQuantity;
		this.bookPublishDate = bookPublishDate;
		this.bookIsbn = bookIsbn;
		this.bookPrice = bookPrice;
		this.bookSalePercent = bookSalePercent;
		this.bookSalePrice = bookSalePrice;

	}

	public void updateQuantitiy(int bookQuantity) {
		this.bookQuantity -= bookQuantity;
	}
}