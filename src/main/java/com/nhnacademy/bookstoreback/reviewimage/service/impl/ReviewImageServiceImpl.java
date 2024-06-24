package com.nhnacademy.bookstoreback.reviewimage.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nhnacademy.bookstoreback.reviewimage.domain.entity.ReviewImage;
import com.nhnacademy.bookstoreback.reviewimage.repository.ReviewImageRepository;
import com.nhnacademy.bookstoreback.reviewimage.service.ReviewImageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewImageServiceImpl implements ReviewImageService {
	private final ReviewImageRepository reviewImageRepository;

	@Override
	public List<ReviewImage> getReviewImages(Long reviewId) {
		return reviewImageRepository.findByReviewReviewId(reviewId);
	}

	@Override
	public ReviewImage saveReviewImage(ReviewImage reviewImage) {
		return reviewImageRepository.save(reviewImage);
	}

	@Override
	public ReviewImage getReviewImageById(Long reviewImageId) {
		return reviewImageRepository.findById(reviewImageId).orElse(null);
	}

	@Override
	public void deleteReviewImage(Long reviewImageId) {
		reviewImageRepository.deleteById(reviewImageId);
	}
}
