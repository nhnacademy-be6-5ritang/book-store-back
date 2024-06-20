package com.nhnacademy.bookstoreback.review.domain.entity;

import java.time.LocalDateTime;

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
@Table(name = "reviews")
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JoinColumn(name = "review_id")
	private Long reviewId;

	@JoinColumn(name = "review_sccore", nullable = false)
	private int reviewScore;

	@JoinColumn(name = "review_comment", nullable = false)
	private String reviewComment;

	@JoinColumn(name = "review_created_at", nullable = false)
	private LocalDateTime reviewCreatedAt;

	@ManyToOne(optional = false)
	@JoinColumn(name = "book_id", nullable = false)
	private Book book;

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	public Review(int reviewScore, String reviewComment, LocalDateTime reviewCreatedAt, Book book, User user) {
		this.reviewScore = reviewScore;
		this.reviewComment = reviewComment;
		this.reviewCreatedAt = reviewCreatedAt;
		this.book = book;
		this.user = user;
	}
}
