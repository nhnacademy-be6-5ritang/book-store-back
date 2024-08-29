package com.nhnacademy.bookstoreback.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.review.domain.entity.Review;

/**
 * @author 이경헌
 * Review 엔티티를 관리하는 Spring Data JPA 리포지토리입니다.
 */
public interface ReviewRepository extends JpaRepository<Review, Long>, CustomReviewRepository {

	/**
	 * 특정 주문 목록 ID에 대한 리뷰의 존재 여부를 확인합니다.
	 *
	 * @param orderListId 주문 목록 ID
	 * @return 해당 주문 목록 ID로 작성된 리뷰가 존재하면 {@code true}, 그렇지 않으면 {@code false}
	 */
	boolean existsByBookOrderOrderListId(Long orderListId);

}
