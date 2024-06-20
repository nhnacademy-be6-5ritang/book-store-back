package com.nhnacademy.bookstoreback.review.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {
	private final ReviewRepository reviewRepository;
	private final BookRepository bookRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public List<Review> findAllReviews() {
		return reviewRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> findReviewsByBookId(Long userId, Long bookId, Pageable pageable) {
		Page<Review> reviews = reviewRepository.findAllByBookBookId(bookId, pageable);
		return reviews
			.map(review -> new GetReviewResponse(userId, bookId, review.getReviewScore(), review.getReviewComment(),
				review.getReviewCreatedAt()));
	}

	@Override
	public CreateReviewResponse saveReview(CreateReviewRequest request) {
		Book book = bookRepository.findById(request.bookId()).orElseThrow(() -> {
			String errorMessage = String.format("해당 도서 '%d'는 존재하지 않는 도서입니다.", request.bookId());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		User user = userRepository.findById(request.userId()).orElseThrow(() -> {
			String errorMessage = String.format("해당 회원 '%d'는 존재하지 않는 회원입니다.", request.userId());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		Review review = new Review(request.reviewScore(), request.reviewComment(), LocalDateTime.now(), book, user);
		reviewRepository.save(review);

		return CreateReviewResponse.builder()
			.reviewId(review.getReviewId())
			.reviewScore(review.getReviewScore())
			.reviewComment(review.getReviewComment())
			.reviewCreatedAt(review.getReviewCreatedAt())
			.userId(user.getId())
			.bookId(book.getBookId())
			.build();
	}

	@Override
	@Transactional(readOnly = true)
	public GetReviewResponse findReviewById(Long reviewId) {
		Review review = reviewRepository.findById(reviewId).orElseThrow(() -> {
			String errorMessage = String.format("해당 리뷰 '%d'는 존재하지 않는 리뷰입니다.", reviewId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		return GetReviewResponse.builder()
			.reviewScore(review.getReviewScore())
			.reviewComment(review.getReviewComment())
			.reviewCreatedAt(review.getReviewCreatedAt())
			.bookId(review.getBook().getBookId())
			.userId(review.getUser().getId())
			.build();
	}

	@Override
	public UpdateReviewResponse updateReview(Long reviewId, UpdateReviewRequest request) {
		Review review = reviewRepository.findById(reviewId).orElseThrow(() -> {
			String errorMessage = String.format("해당 리뷰 '%d'는 존재하지 않는 리뷰입니다.", reviewId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		review.updateReviewScore(request.reviewScore(), request.reviewComment());
		reviewRepository.save(review);

		return UpdateReviewResponse.builder()
			.reviewComment(review.getReviewComment())
			.reviewScore(review.getReviewScore())
			.build();
	}

	@Override
	public void deleteReview(Long reviewId) {
		reviewRepository.deleteById(reviewId);
	}
}
