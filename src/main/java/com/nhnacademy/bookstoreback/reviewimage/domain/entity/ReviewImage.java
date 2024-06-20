package com.nhnacademy.bookstoreback.reviewimage.domain.entity;

import java.time.LocalDateTime;

import com.nhnacademy.bookstoreback.review.domain.entity.Review;

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
@Table(name = "reviews_images")
public class ReviewImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JoinColumn(name = "review_image_id")
	private Long reviewImageId;

	@JoinColumn(name = "review_iamge_name")
	private String reviewImageName;

	@JoinColumn(name = "review_image_url")
	private String reviewImageUrl;

	@JoinColumn(name = "review_image_created_at")
	private LocalDateTime reviewImageCreatedAt;

	@ManyToOne(optional = false)
	@JoinColumn(name = "review_id")
	private Review review;
}
