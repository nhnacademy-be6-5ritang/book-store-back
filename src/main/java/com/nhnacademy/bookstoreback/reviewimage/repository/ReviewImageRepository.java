package com.nhnacademy.bookstoreback.reviewimage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.reviewimage.domain.entity.ReviewImage;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
	List<ReviewImage> findByReviewReviewId(Long reviewId);
}
