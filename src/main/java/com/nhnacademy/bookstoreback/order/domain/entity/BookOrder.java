package com.nhnacademy.bookstoreback.order.domain.entity;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "books_and_orders")
public class BookOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_list_id")
	private Long orderListId;

	@Column(name = "book_quantity")
	private BigDecimal bookQuantity;

	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;

	@OneToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@Builder
	public BookOrder(
		BigDecimal bookQuantity,
		Book book,
		Order order) {
		this.bookQuantity = bookQuantity;
		this.book = book;
		this.order = order;
	}

	public static BookOrder toEntity(BigDecimal bookQuantity, Book book, Order order) {
		return BookOrder.builder()
			.book(book)
			.order(order)
			.bookQuantity(bookQuantity)
			.build();
	}

}
