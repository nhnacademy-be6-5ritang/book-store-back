package com.nhnacademy.bookstoreback.reviewimage.service;

import java.util.List;

import com.nhnacademy.bookstoreback.reviewimage.domain.entity.ReviewImage;

public interface ReviewImageService {
	List<ReviewImage> getReviewImages(Long reviewId);

	ReviewImage saveReviewImage(ReviewImage reviewImage);

	ReviewImage getReviewImageById(Long reviewImageId);

	void deleteReviewImage(Long reviewImageId);
}
