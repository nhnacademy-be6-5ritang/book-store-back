package com.nhnacademy.bookstoreback.review.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.review.domain.dto.request.CreateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.request.UpdateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.response.GetBookOrderWithoutReviewResponse;
import com.nhnacademy.bookstoreback.review.domain.dto.response.GetReviewResponse;

/**
 * @author 이경헌
 * 리뷰 관련 기능을 제공하는 서비스 인터페이스입니다.
 */
public interface ReviewService {

	/**
	 * 모든 리뷰를 페이지네이션하여 조회합니다.
	 *
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 페이지네이션된 모든 리뷰 목록 (Page 객체)
	 */
	Page<GetReviewResponse> getReviews(Pageable pageable);

	/**
	 * 모든 사진 리뷰들을 페이지네이션하여 조회합니다.
	 *
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 페이지네이션된 특정 책의 사진 리뷰 목록 (Page 객체)
	 */
	Page<GetReviewResponse> getPhotoReviews(Pageable pageable);

	/**
	 * 모든 일반 리뷰들을 페이지네이션하여 조회합니다.
	 *
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 페이지네이션된 특정 책의 일반 리뷰 목록 (Page 객체)
	 */
	Page<GetReviewResponse> getGeneralReviews(Pageable pageable);

	/**
	 * 특정 책의 리뷰들을 페이지네이션하여 조회합니다.
	 *
	 * @param bookId   책의 ID
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 페이지네이션된 특정 책의 리뷰 목록 (Page 객체)
	 */
	Page<GetReviewResponse> getReviewsByBookId(Long bookId, Pageable pageable);

	/**
	 * 특정 책의 일반 리뷰들을 페이지네이션하여 조회합니다.
	 *
	 * @param bookId   책의 ID
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 페이지네이션된 특정 책의 일반 리뷰 목록 (Page 객체)
	 */
	Page<GetReviewResponse> getGeneralReviewsByBookId(Long bookId, Pageable pageable);

	/**
	 * 특정 책의 사진 리뷰들을 페이지네이션하여 조회합니다.
	 *
	 * @param bookId   책의 ID
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 페이지네이션된 특정 책의 사진 리뷰 목록 (Page 객체)
	 */
	Page<GetReviewResponse> getPhotoReviewsByBookId(Long bookId, Pageable pageable);

	/**
	 * 특정 사용자의 리뷰들을 페이지네이션하여 조회합니다.
	 *
	 * @param pageable    페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @param currentUser 현재 사용자의 정보 (사용자 ID 등)
	 * @return 페이지네이션된 특정 사용자의 리뷰 목록 (Page 객체)
	 */
	Page<GetReviewResponse> getReviewsByUserId(Pageable pageable, CurrentUserDetails currentUser);

	/**
	 * 특정 사용자의 일반 리뷰들을 페이지네이션하여 조회합니다.
	 *
	 * @param pageable    페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @param currentUser 현재 사용자의 정보 (사용자 ID 등)
	 * @return 페이지네이션된 특정 사용자의 일반 리뷰 목록 (Page 객체)
	 */
	Page<GetReviewResponse> getGeneralReviewsByUserId(Pageable pageable, CurrentUserDetails currentUser);

	/**
	 * 특정 사용자의 사진 리뷰들을 페이지네이션하여 조회합니다.
	 *
	 * @param pageable    페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @param currentUser 현재 사용자의 정보 (사용자 ID 등)
	 * @return 페이지네이션된 특정 사용자의 사진 리뷰 목록 (Page 객체)
	 */
	Page<GetReviewResponse> getPhotoReviewsByUserId(Pageable pageable, CurrentUserDetails currentUser);

	/**
	 * 새로운 리뷰를 저장합니다.
	 *
	 * @param request     새로 저장할 리뷰의 정보 (작성자 ID, 책 ID, 평점, 코멘트 등)
	 * @param currentUser 현재 사용자의 정보 (사용자 ID 등)
	 */
	void createReview(CreateReviewRequest request, CurrentUserDetails currentUser);

	/**
	 * 주어진 ID에 해당하는 리뷰를 조회합니다.
	 *
	 * @param id 리뷰 ID
	 * @return 조회된 리뷰의 정보 (작성자 ID, 책 ID, 평점, 코멘트 등)
	 */
	GetReviewResponse findReviewById(Long id);

	/**
	 * 주어진 ID에 해당하는 리뷰를 수정합니다.
	 *
	 * @param reviewId 리뷰 ID
	 * @param request  수정할 리뷰의 정보 (수정할 평점, 코멘트 등)
	 */
	void updateReview(Long reviewId, UpdateReviewRequest request);

	/**
	 * 주어진 ID에 해당하는 리뷰를 삭제합니다.
	 *
	 * @param reviewId 삭제할 리뷰의 ID
	 */
	void deleteReview(Long reviewId);

	/**
	 * 특정 책의 리뷰 평균 점수를 조회합니다.
	 *
	 * @param bookId 책의 ID
	 * @return 책의 리뷰 평균 점수
	 */
	double getReviewsAverageScoreByBookId(Long bookId);

	/**
	 * 특정 사용자가 완료한 주문에 따라 해당 사용자가 리뷰할 수 있는 책 목록을 조회합니다.
	 *
	 * @param currentUser 현재 사용자의 정보 (사용자 ID 등)
	 * @return 사용자가 리뷰할 수 있는 책 목록
	 */
	List<GetBookOrderWithoutReviewResponse> getBooksWithoutReviews(CurrentUserDetails currentUser);
}