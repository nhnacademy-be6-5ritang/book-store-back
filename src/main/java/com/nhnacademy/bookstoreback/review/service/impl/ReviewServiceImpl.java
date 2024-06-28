package com.nhnacademy.bookstoreback.review.service.impl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.review.domain.dto.request.CreateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.request.UpdateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.response.CreateReviewResponse;
import com.nhnacademy.bookstoreback.review.domain.dto.response.GetReviewResponse;
import com.nhnacademy.bookstoreback.review.domain.dto.response.UpdateReviewResponse;
import com.nhnacademy.bookstoreback.review.domain.entity.Review;
import com.nhnacademy.bookstoreback.review.repository.ReviewRepository;
import com.nhnacademy.bookstoreback.review.service.ReviewService;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * @author
 * 리뷰 서비스 구현체 클래스입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {
	private final ReviewRepository reviewRepository;
	private final BookRepository bookRepository;
	private final UserRepository userRepository;

	/**
	 * 모든 리뷰를 페이지네이션하여 조회합니다.
	 *
	 * @param pageable 페이지네이션 정보
	 * @return 페이지네이션된 리뷰 응답
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> findAllReviews(Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 5;

		return reviewRepository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "reviewCreatedAt")))
			.map(GetReviewResponse::fromEntity);
	}

	/**
	 * 책 ID를 기준으로 리뷰를 페이지네이션하여 조회합니다.
	 *
	 * @param bookId 책의 ID
	 * @param pageable 페이지네이션 정보
	 * @return 페이지네이션된 리뷰 응답
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> findReviewsByBookId(Long bookId, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 5;

		Page<Review> reviews = reviewRepository.findAllByBookBookId(bookId,
			PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "reviewCreatedAt")));

		return reviews
			.map(GetReviewResponse::fromEntity);
	}

	/**
	 * 사용자 ID를 기준으로 리뷰를 페이지네이션하여 조회합니다.
	 *
	 * @param userId 사용자의 ID
	 * @param pageable 페이지네이션 정보
	 * @return 페이지네이션된 리뷰 응답
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> findReviewsByUserId(Long userId, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 5;

		Page<Review> reviews = reviewRepository.findAllByUserId(userId,
			PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "reviewCreatedAt")));

		return reviews
			.map(GetReviewResponse::fromEntity);
	}

	/**
	 * 새로운 리뷰를 저장합니다.
	 *
	 * @param request 리뷰 생성 요청 DTO
	 * @return 생성된 리뷰 응답 DTO
	 */
	@Override
	public CreateReviewResponse saveReview(CreateReviewRequest request) {
		User user = userRepository.findById(request.userId()).orElseThrow(() -> {
			String errorMessage = String.format("해당 회원 '%d'는 존재하지 않는 회원입니다.", request.userId());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		Book book = bookRepository.findById(request.bookId()).orElseThrow(() -> {
			String errorMessage = String.format("해당 도서 '%d'는 존재하지 않는 도서입니다.", request.bookId());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		Review review = new Review(request.reviewScore(), request.reviewComment(), LocalDateTime.now(), book, user);
		reviewRepository.save(review);

		return CreateReviewResponse.fromEntity(review, user, book);
	}

	/**
	 * 리뷰 ID를 기준으로 리뷰를 조회합니다.
	 *
	 * @param reviewId 리뷰의 ID
	 * @return 조회된 리뷰 응답 DTO
	 */
	@Override
	@Transactional(readOnly = true)
	public GetReviewResponse findReviewById(Long reviewId) {
		Review review = reviewRepository.findById(reviewId).orElseThrow(() -> {
			String errorMessage = String.format("해당 리뷰 '%d'는 존재하지 않는 리뷰입니다.", reviewId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		return GetReviewResponse.fromEntity(review);
	}

	/**
	 * 리뷰를 업데이트합니다.
	 *
	 * @param reviewId 리뷰의 ID
	 * @param request 리뷰 업데이트 요청 DTO
	 * @return 업데이트된 리뷰 응답 DTO
	 */
	@Override
	public UpdateReviewResponse updateReview(Long reviewId, UpdateReviewRequest request) {
		Review review = reviewRepository.findById(reviewId).orElseThrow(() -> {
			String errorMessage = String.format("해당 리뷰 '%d'는 존재하지 않는 리뷰입니다.", reviewId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		review.updateReviewScore(request.reviewScore(), request.reviewComment());
		reviewRepository.save(review);

		return UpdateReviewResponse.fromEntity(review);
	}

	/**
	 * 리뷰를 삭제합니다.
	 *
	 * @param reviewId 리뷰의 ID
	 */
	@Override
	public void deleteReview(Long reviewId) {
		reviewRepository.deleteById(reviewId);
	}
}
