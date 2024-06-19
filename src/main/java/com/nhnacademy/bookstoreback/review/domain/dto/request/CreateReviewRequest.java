package com.nhnacademy.bookstoreback.review.domain.dto.request;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record CreateReviewRequest(Long bookId, Long userId, int reviewScore, String reviewComment,
								  LocalDateTime reviewCreatedAt) {
}
