package com.nhnacademy.bookstoreback.review.domain.dto.request;

import lombok.Builder;

@Builder
public record UpdateReviewRequest(Long reviewId, int reviewScore, String reviewComment) {
}
