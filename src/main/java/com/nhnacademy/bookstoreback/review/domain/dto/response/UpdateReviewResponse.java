package com.nhnacademy.bookstoreback.review.domain.dto.response;

import lombok.Builder;

@Builder
public record UpdateReviewResponse(int reviewScore, String reviewComment) {
}
