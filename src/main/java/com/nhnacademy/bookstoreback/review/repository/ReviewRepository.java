package com.nhnacademy.bookstoreback.review.repository;

import java.util.List;

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
	 * 모든 리뷰중 이미지가 없는 리뷰(Review)들을 페이지네이션하여 조회합니다.
	 *
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 이미지가 없는 해당 책의 리뷰들을 페이지네이션한 결과 (Page 객체)
	 */
	Page<Review> findAllByReviewImagesEmpty(Pageable pageable);

	/**
	 * 모든 리뷰중 이미지가 있는 리뷰(Review)들을 페이지네이션하여 조회합니다.
	 *
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 이미지가 있는 해당 책의 리뷰들을 페이지네이션한 결과 (Page 객체)
	 */
	Page<Review> findAllByReviewImagesNotEmpty(Pageable pageable);

	/**
	 * 책(Book)의 ID를 기반으로 리뷰(Review)들을 페이지네이션하여 조회합니다.
	 *
	 * @param bookId   책의 ID
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 해당 책의 리뷰들을 페이지네이션한 결과 (Page 객체)
	 */
	Page<Review> findAllByBookOrderBookBookId(Long bookId, Pageable pageable);

	/**
	 * 책(Book)의 ID를 기반으로 이미지가 없는 리뷰(Review)들을 페이지네이션하여 조회합니다.
	 *
	 * @param bookId   책의 ID
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 이미지가 없는 해당 책의 리뷰들을 페이지네이션한 결과 (Page 객체)
	 */
	Page<Review> findAllByBookOrderBookBookIdAndReviewImagesEmpty(Long bookId, Pageable pageable);

	/**
	 * 책(Book)의 ID를 기반으로 이미지가 있는 리뷰(Review)들을 페이지네이션하여 조회합니다.
	 *
	 * @param bookId   책의 ID
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 이미지가 있는 해당 책의 리뷰들을 페이지네이션한 결과 (Page 객체)
	 */
	Page<Review> findAllByBookOrderBookBookIdAndReviewImagesNotEmpty(Long bookId, Pageable pageable);

	/**
	 * 특정 사용자(User)의 ID를 기반으로 리뷰(Review)들을 페이지네이션하여 조회합니다.
	 *
	 * @param userId   사용자의 ID
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 해당 사용자의 리뷰들을 페이지네이션한 결과 (Page 객체)
	 */
	Page<Review> findAllByUserId(Long userId, Pageable pageable);

	/**
	 * 특정 사용자(User)의 ID를 기반으로 이미지가 없는 리뷰(Review)들을 페이지네이션하여 조회합니다.
	 *
	 * @param userId   사용자의 ID
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 이미지가 없는 해당 사용자의 리뷰들을 페이지네이션한 결과 (Page 객체)
	 */
	Page<Review> findAllByUserIdAndReviewImagesEmpty(Long userId, Pageable pageable);

	/**
	 * 특정 사용자(User)의 ID를 기반으로 이미지가 있는 리뷰(Review)들을 페이지네이션하여 조회합니다.
	 *
	 * @param userId   사용자의 ID
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 이미지가 있는 해당 사용자의 리뷰들을 페이지네이션한 결과 (Page 객체)
	 */
	Page<Review> findAllByUserIdAndReviewImagesNotEmpty(Long userId, Pageable pageable);

	/**
	 * 특정 주문 목록 ID에 대한 리뷰의 존재 여부를 확인합니다.
	 *
	 * @param orderListId 주문 목록 ID
	 * @return 해당 주문 목록 ID로 작성된 리뷰가 존재하면 {@code true}, 그렇지 않으면 {@code false}
	 */
	boolean existsByBookOrderOrderListId(Long orderListId);

	/**
	 * 특정 사용자의 ID를 기반으로 모든 리뷰를 조회합니다.
	 *
	 * @param userId 사용자의 ID
	 * @return 해당 사용자의 모든 리뷰 리스트
	 */
	List<Review> findAllByUserId(Long userId);
}
