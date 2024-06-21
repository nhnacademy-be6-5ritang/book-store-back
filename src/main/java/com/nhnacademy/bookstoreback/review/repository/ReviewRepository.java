package com.nhnacademy.bookstoreback.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.review.domain.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	/**
	 * 책(Book)의 ID를 기반으로 리뷰(Review)들을 페이지네이션하여 조회합니다.
	 *
	 * @param bookId 책의 ID
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 해당 책의 리뷰들을 페이지네이션한 결과 (Page 객체)
	 */
	Page<Review> findAllByBookBookId(Long bookId, Pageable pageable);
}
