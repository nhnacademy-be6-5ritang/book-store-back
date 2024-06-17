package com.nhnacademy.bookstoreback.review.controller;

import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;
}
