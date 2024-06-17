package com.nhnacademy.bookstoreback.review.domain.entity;

import java.time.LocalDateTime;

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
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reviewId;

	private int reviewScore;
	private String reviewComment;
	private LocalDateTime reviewCreatedAt;

	// @ManyToOne
	// @JoinColumn(name = "book_id")
	// private Book book;
	//

	private Long userId;
}
