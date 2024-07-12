package com.nhnacademy.bookstoreback.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.review.domain.entity.ReviewImage;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
	ReviewImage findByReviewReviewId(Long reviewId);
}
