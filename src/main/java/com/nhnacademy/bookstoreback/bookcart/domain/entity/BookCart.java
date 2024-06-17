package com.nhnacademy.bookstoreback.bookcart.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BookCart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookCartId;

	private int bookQuantity;

	// @ManyToOne(optional = false)
	// @JoinColumn(name = "book_id")
	// private Book book;
	//
	// @ManyToOne(optional = false)
	// @JoinColumn(name = "cart_id")
	// private Cart cart;
}
