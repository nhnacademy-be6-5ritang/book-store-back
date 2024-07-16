package com.nhnacademy.bookstoreback.review.domain.entity;

import com.nhnacademy.bookstoreback.image.domain.entity.Image;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reviews_images")
public class ReviewImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_image_id")
	private Long reviewImageId;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "image_id", nullable = false)
	private Image image;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "review_id", nullable = false)
	private Review review;

	@Builder
	public ReviewImage(Image image, Review review) {
		this.image = image;
		this.review = review;
	}

	public static ReviewImage toEntity(Review review, Image image) {
		return ReviewImage.builder()
			.review(review)
			.image(image)
			.build();
	}
}
