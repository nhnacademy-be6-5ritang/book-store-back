package com.nhnacademy.bookstoreback.review.domain.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record GetReviewResponse(Long bookId, Long userId, int reviewScore, String reviewComment,
								LocalDateTime reviewCreatedAt) {
}
