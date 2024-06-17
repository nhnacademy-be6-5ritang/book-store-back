package com.nhnacademy.bookstoreback.reviewimage.domain.entity;

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
public class ReviewImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reviewImageId;

	private String reviewImageName;
	private String reviewImageUrl;
	private LocalDateTime reviewImageCreateAt;

	// @ManyToOne(optional = false)
	// @JoinColumn(name = "review_id")
	// private Review review;
}
