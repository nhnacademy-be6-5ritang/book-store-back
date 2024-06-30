package com.nhnacademy.bookstoreback.reviewimage.domain.entity;

import com.nhnacademy.bookstoreback.image.domain.entity.Image;
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

	@ManyToOne(optional = false)
	@JoinColumn(name = "image_id")
	private Image image;

	@ManyToOne(optional = false)
	@JoinColumn(name = "review_id")
	private Review review;
}
