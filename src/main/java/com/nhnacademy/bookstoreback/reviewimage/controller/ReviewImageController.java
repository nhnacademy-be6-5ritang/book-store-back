package com.nhnacademy.bookstoreback.reviewimage.controller;

import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.reviewimage.service.ReviewImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReviewImageController {
	private final ReviewImageService reviewImageService;

}
