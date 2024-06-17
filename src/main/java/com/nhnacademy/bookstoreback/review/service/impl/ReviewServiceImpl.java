package com.nhnacademy.bookstoreback.review.service.impl;

import org.springframework.stereotype.Service;

import com.nhnacademy.bookstoreback.review.repository.ReviewRepository;
import com.nhnacademy.bookstoreback.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	private final ReviewRepository reviewRepository;
}
