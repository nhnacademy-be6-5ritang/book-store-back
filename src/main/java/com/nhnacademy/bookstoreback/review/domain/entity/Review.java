package com.nhnacademy.bookstoreback.review.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "reviews")
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reviewId;

	private int reviewScore;
	private String reviewComment;
	private LocalDateTime reviewCreatedAt;

	// @ManyToOne(optional = false)
	// @JoinColumn(name = "book_id")
	// private Book book;

	// @ManyToOne(optional = false)
	// @JoinColumn(name = "user_id")
	// private User user;
}
