package com.nhnacademy.bookstoreback.review.domain.dto.request;

import lombok.Builder;

@Builder
public record CreateReviewRequest(Long bookId, Long userId, int reviewScore, String reviewComment) {
}
