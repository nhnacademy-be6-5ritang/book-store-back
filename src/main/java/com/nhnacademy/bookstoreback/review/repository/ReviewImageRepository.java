package com.nhnacademy.bookstoreback.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.review.domain.entity.ReviewImage;

/**
 * @author 이경헌
 * ReviewImage 엔티티를 관리하는 Spring Data JPA 리포지토리입니다.
 */
public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
	/**
	 * 주어진 리뷰 ID에 해당하는 ReviewImage 엔티티를 반환합니다.
	 *
	 * @param reviewId 리뷰의 ID
	 * @return 주어진 리뷰 ID에 해당하는 ReviewImage 엔티티, 존재하지 않을 경우 null 을 반환할 수 있습니다.
	 */
	ReviewImage findByReviewReviewId(Long reviewId);

	/**
	 * 주어진 리뷰 ID에 해당하는 모든 {@link ReviewImage} 엔티티를 삭제합니다.
	 *
	 * @param reviewId 삭제할 리뷰의 ID
	 */
	void deleteAllByReview_ReviewId(Long reviewId);
}
