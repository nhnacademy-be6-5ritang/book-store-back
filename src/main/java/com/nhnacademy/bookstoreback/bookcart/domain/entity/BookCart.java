package com.nhnacademy.bookstoreback.bookcart.domain.entity;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.cart.domain.entity.Cart;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "books_and_carts")
public class BookCart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JoinColumn(name = "book_cart_id")
	private Long bookCartId;

	@JoinColumn(name = "book_quantity", nullable = false)
	private int bookQuantity;

	@ManyToOne(optional = false)
	@JoinColumn(name = "book_id", nullable = false)
	private Book book;

	@ManyToOne(optional = false)
	@JoinColumn(name = "cart_id", nullable = false)
	private Cart cart;

	public BookCart(Book book, Cart cart, int bookQuantity) {
		this.bookQuantity = bookQuantity;
		this.book = book;
		this.cart = cart;
	}
}
