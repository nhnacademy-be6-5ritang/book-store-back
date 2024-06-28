package com.nhnacademy.bookstoreback.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.review.domain.entity.Review;

/**
 * @author 이경헌
 * Review 엔티티를 관리하는 Spring Data JPA 리포지토리입니다.
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {
	/**
	 * 책(Book)의 ID를 기반으로 리뷰(Review)들을 페이지네이션하여 조회합니다.
	 *
	 * @param bookId 책의 ID
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 해당 책의 리뷰들을 페이지네이션한 결과 (Page 객체)
	 */
	Page<Review> findAllByBookBookId(Long bookId, Pageable pageable);

	/**
	 * 특정 사용자(User)의 ID를 기반으로 리뷰(Review)들을 페이지네이션하여 조회합니다.
	 *
	 * @param userId   사용자의 ID
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 해당 사용자의 리뷰들을 페이지네이션한 결과 (Page 객체)
	 */
	Page<Review> findAllByUserId(Long userId, Pageable pageable);
}
