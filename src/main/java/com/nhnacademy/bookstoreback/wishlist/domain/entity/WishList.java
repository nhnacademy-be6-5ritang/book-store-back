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
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author
 * 위시리스트 엔티티 클래스.
 * 각 위시리스트는 특정 사용자와 특정 책을 참조합니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "wish_lists")
public class WishList {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long wishListId;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "book_id", nullable = false)
	private Book book;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	public WishList(Book book, User user) {
		this.book = book;
		this.user = user;
	}

}
