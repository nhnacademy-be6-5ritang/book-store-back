package com.nhnacademy.bookstoreback.reviewimage.service.impl;

import org.springframework.stereotype.Service;

import com.nhnacademy.bookstoreback.reviewimage.repository.ReviewImageRepository;
import com.nhnacademy.bookstoreback.reviewimage.service.ReviewImageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewImageServiceImpl implements ReviewImageService {
	private final ReviewImageRepository reviewImageRepository;
}
