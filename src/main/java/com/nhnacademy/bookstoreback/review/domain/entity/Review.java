package com.nhnacademy.bookstoreback.review.domain.entity;

import java.time.LocalDateTime;

import com.nhnacademy.bookstoreback.order.domain.entity.BookOrder;
import com.nhnacademy.bookstoreback.review.domain.dto.request.CreateReviewRequest;
import com.nhnacademy.bookstoreback.user.domain.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

	@NotNull
	@Column(name = "review_score", nullable = false)
	private int reviewScore;

	@NotBlank
	@Size(max = 400)
	@Column(name = "review_comment", nullable = false, length = 400)
	private String reviewComment;

	@NotNull
	@Column(name = "review_created_at", nullable = false)
	private LocalDateTime reviewCreatedAt = LocalDateTime.now();

	@NotNull
	@OneToOne(optional = false)
	@JoinColumn(name = "order_list_id", nullable = false)
	private BookOrder bookOrder;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Builder
	public Review(int reviewScore, String reviewComment, BookOrder bookOrder, User user) {
		this.reviewScore = reviewScore;
		this.reviewComment = reviewComment;
		this.bookOrder = bookOrder;
		this.user = user;
	}

	public static Review toEntity(CreateReviewRequest request, BookOrder bookOrder, User user) {
		return Review.builder()
			.reviewScore(request.reviewScore())
			.reviewComment(request.reviewComment())
			.bookOrder(bookOrder)
			.user(user)
			.build();
	}

	public void updateReviewScore(int newScore, String reviewComment) {
		this.reviewScore = newScore;
		this.reviewComment = reviewComment;
	}

}
