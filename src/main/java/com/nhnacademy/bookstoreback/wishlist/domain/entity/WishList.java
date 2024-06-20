package com.nhnacademy.bookstoreback.wishlist.domain.entity;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.user.domain.entity.User;

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
@Table(name = "wish_lists")
public class WishList {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long wishListId;

	@ManyToOne(optional = false)
	@JoinColumn(name = "book_id")
	private Book book;

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	public WishList(Book book, User user) {
		this.book = book;
		this.user = user;
	}

}
