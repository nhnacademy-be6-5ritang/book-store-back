package com.nhnacademy.bookstoreback.review.domain.dto.request;

import lombok.Builder;

@Builder
public record GetReviewsByUserRequest(Long userId) {
}
