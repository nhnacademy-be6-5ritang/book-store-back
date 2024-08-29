package com.nhnacademy.bookstoreback.review.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstoreback.review.domain.dto.response.GetBookOrderWithoutReviewResponse;
import com.nhnacademy.bookstoreback.review.domain.dto.response.GetReviewResponse;

/**
 * @author 이경헌
 * 커스텀 리뷰 레포지토리 인터페이스입니다.
 */
public interface CustomReviewRepository {

	/**
	 * 주어진 ID에 해당하는 리뷰를 조회합니다.
	 *
	 * @param reviewId 리뷰 ID
	 * @return 조회된 리뷰의 정보 (작성자 ID, 책 ID, 평점, 코멘트 등)
	 */
	Optional<GetReviewResponse> getReview(Long reviewId);

	/**
	 * 모든 리뷰를 페이지네이션하여 조회합니다.
	 *
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 이미지가 없는 해당 책의 리뷰들을 페이지네이션한 결과 (Page 객체)
	 */
	Page<GetReviewResponse> getReviews(Pageable pageable);

	/**
	 * 모든 리뷰중 이미지가 없는 리뷰(Review)들을 페이지네이션하여 조회합니다.
	 *
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 이미지가 없는 해당 책의 리뷰들을 페이지네이션한 결과 (Page 객체)
	 */
	Page<GetReviewResponse> getGeneralReviews(Pageable pageable);

	/**
	 * 모든 리뷰중 이미지가 있는 리뷰(Review)들을 페이지네이션하여 조회합니다.
	 *
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 이미지가 있는 해당 책의 리뷰들을 페이지네이션한 결과 (Page 객체)
	 */
	Page<GetReviewResponse> getPhotoReviews(Pageable pageable);

	/**
	 * 책(Book)의 ID를 기반으로 리뷰(Review)들을 페이지네이션하여 조회합니다.
	 *
	 * @param bookId   책의 ID
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 해당 책의 리뷰들을 페이지네이션한 결과 (Page 객체)
	 */
	Page<GetReviewResponse> getReviewsByBookId(Long bookId, Pageable pageable);

	/**
	 * 책(Book)의 ID를 기반으로 이미지가 없는 리뷰(Review)들을 페이지네이션하여 조회합니다.
	 *
	 * @param bookId   책의 ID
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 이미지가 없는 해당 책의 리뷰들을 페이지네이션한 결과 (Page 객체)
	 */
	Page<GetReviewResponse> getGeneralReviewsByBookId(Long bookId, Pageable pageable);

	/**
	 * 책(Book)의 ID를 기반으로 이미지가 있는 리뷰(Review)들을 페이지네이션하여 조회합니다.
	 *
	 * @param bookId   책의 ID
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 이미지가 있는 해당 책의 리뷰들을 페이지네이션한 결과 (Page 객체)
	 */
	Page<GetReviewResponse> getPhotoReviewsByBookId(Long bookId, Pageable pageable);

	/**
	 * 특정 사용자(User)의 ID를 기반으로 리뷰(Review)들을 페이지네이션하여 조회합니다.
	 *
	 * @param userId   사용자의 ID
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 해당 사용자의 리뷰들을 페이지네이션한 결과 (Page 객체)
	 */
	Page<GetReviewResponse> getReviewsByUserId(Long userId, Pageable pageable);

	/**
	 * 특정 사용자(User)의 ID를 기반으로 이미지가 없는 리뷰(Review)들을 페이지네이션하여 조회합니다.
	 *
	 * @param userId   사용자의 ID
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 이미지가 없는 해당 사용자의 리뷰들을 페이지네이션한 결과 (Page 객체)
	 */
	Page<GetReviewResponse> getGeneralReviewsByUserId(Long userId, Pageable pageable);

	/**
	 * 특정 사용자(User)의 ID를 기반으로 이미지가 있는 리뷰(Review)들을 페이지네이션하여 조회합니다.
	 *
	 * @param userId   사용자의 ID
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 이미지가 있는 해당 사용자의 리뷰들을 페이지네이션한 결과 (Page 객체)
	 */
	Page<GetReviewResponse> getPhotoReviewsByUserId(Long userId, Pageable pageable);

	/**
	 * 특정 도서 ID에 대한 리뷰의 평균 점수를 조회합니다.
	 *
	 * @param bookId 조회할 도서의 ID
	 * @return 주어진 도서 ID에 대한 리뷰 점수의 평균
	 */
	double getReviewsAverageScoreByBookId(Long bookId);

	/**
	 * 특정 사용자가 완료한 주문에 따라 해당 사용자가 리뷰할 수 있는 책 목록을 조회합니다.
	 *
	 * @param userId 현재 사용자의 정보 (사용자 ID 등)
	 * @return 사용자가 리뷰할 수 있는 책 목록
	 */
	List<GetBookOrderWithoutReviewResponse> getBooksWithoutReviewsByUserId(Long userId);
}
