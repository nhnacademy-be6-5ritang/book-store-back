package com.nhnacademy.bookstoreback.wishlist.domain.entity;

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
public class WishList {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long wishListId;

	private Long userId;

	// @ManyToOne(optional = false)
	// @JoinColumn(name = "book_id")
	// private Book book;

}
