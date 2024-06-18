package com.nhnacademy.bookstoreback.review.domain.dto.response;

import java.util.List;

import com.nhnacademy.bookstoreback.review.domain.entity.Review;

import lombok.Builder;

@Builder
public record GetReviewsByUserResponse(List<Review> reviews) {
}
