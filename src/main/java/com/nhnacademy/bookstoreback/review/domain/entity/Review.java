package com.nhnacademy.bookstoreback.review.domain.entity;

import java.time.LocalDateTime;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.review.domain.dto.request.CreateReviewRequest;
import com.nhnacademy.bookstoreback.user.domain.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "reviews")
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_id")
	private Long reviewId;

	@Column(name = "review_score", nullable = false)
	private int reviewScore;

	@Column(name = "review_comment", nullable = false)
	private String reviewComment;

	@Column(name = "review_created_at", nullable = false)
	private LocalDateTime reviewCreatedAt = LocalDateTime.now();

	@ManyToOne(optional = false)
	@JoinColumn(name = "book_id", nullable = false)
	private Book book;

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Builder
	public Review(int reviewScore, String reviewComment, LocalDateTime reviewCreatedAt, Book book, User user) {
		this.reviewScore = reviewScore;
		this.reviewComment = reviewComment;
		this.reviewCreatedAt = reviewCreatedAt;
		this.book = book;
		this.user = user;
	}

	public static Review toEntity(CreateReviewRequest request, Book book, User user) {
		return Review.builder()
			.reviewScore(request.reviewScore())
			.reviewComment(request.reviewComment())
			.book(book)
			.user(user)
			.build();
	}

	public void updateReviewScore(int newScore, String reviewComment) {
		this.reviewScore = newScore;
		this.reviewComment = reviewComment;
	}

}
