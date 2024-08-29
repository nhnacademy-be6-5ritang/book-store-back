package com.nhnacademy.bookstoreback.review.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.book.exception.BookNotFoundException;
import com.nhnacademy.bookstoreback.global.exception.AccessDeniedException;
import com.nhnacademy.bookstoreback.global.util.ImageUtil;
import com.nhnacademy.bookstoreback.image.domain.entity.Image;
import com.nhnacademy.bookstoreback.image.repository.ImageRepository;
import com.nhnacademy.bookstoreback.order.domain.entity.BookOrder;
import com.nhnacademy.bookstoreback.order.repository.BookOrderRepository;
import com.nhnacademy.bookstoreback.review.domain.dto.request.CreateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.request.UpdateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.response.GetBookOrderWithoutReviewResponse;
import com.nhnacademy.bookstoreback.review.domain.dto.response.GetReviewResponse;
import com.nhnacademy.bookstoreback.review.domain.entity.Review;
import com.nhnacademy.bookstoreback.review.domain.entity.ReviewImage;
import com.nhnacademy.bookstoreback.review.exception.ReviewAlreadyExistsException;
import com.nhnacademy.bookstoreback.review.exception.ReviewNotFoundException;
import com.nhnacademy.bookstoreback.review.repository.ReviewImageRepository;
import com.nhnacademy.bookstoreback.review.repository.ReviewRepository;
import com.nhnacademy.bookstoreback.review.service.ReviewService;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {
	private final UserRepository userRepository;
	private final ReviewRepository reviewRepository;
	private final ReviewImageRepository reviewImageRepository;
	private final ImageRepository imageRepository;
	private final BookOrderRepository bookOrderRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> getReviews(Pageable pageable) {
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();

		return reviewRepository.getReviews(
			PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "reviewCreatedAt")));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> getGeneralReviews(Pageable pageable) {
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();

		return reviewRepository.getGeneralReviews(
			PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "reviewCreatedAt")));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> getPhotoReviews(Pageable pageable) {
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();

		return reviewRepository.getPhotoReviews(
			PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "reviewCreatedAt")));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> getReviewsByBookId(Long bookId, Pageable pageable) {
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();

		return reviewRepository.getReviewsByBookId(bookId,
			PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "reviewCreatedAt")));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> getGeneralReviewsByBookId(Long bookId, Pageable pageable) {
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();

		return reviewRepository.getGeneralReviewsByBookId(bookId,
			PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "reviewCreatedAt")));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> getPhotoReviewsByBookId(Long bookId, Pageable pageable) {
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();

		return reviewRepository.getPhotoReviewsByBookId(bookId,
			PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "reviewCreatedAt")));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> getReviewsByUserId(Pageable pageable, CurrentUserDetails currentUser) {
		Long userId = currentUser != null ? currentUser.getUserId() : null;
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();

		return reviewRepository.getReviewsByUserId(userId,
			PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "reviewCreatedAt")));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> getGeneralReviewsByUserId(Pageable pageable, CurrentUserDetails currentUser) {
		Long userId = currentUser != null ? currentUser.getUserId() : null;
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();

		return reviewRepository.getGeneralReviewsByUserId(userId,
			PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "reviewCreatedAt")));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> getPhotoReviewsByUserId(Pageable pageable, CurrentUserDetails currentUser) {
		Long userId = currentUser != null ? currentUser.getUserId() : null;
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();

		return reviewRepository.getPhotoReviewsByUserId(userId,
			PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "reviewCreatedAt")));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createReview(CreateReviewRequest request, CurrentUserDetails currentUser) {
		Long userId = currentUser != null ? currentUser.getUserId() : null;

		BookOrder bookOrder = bookOrderRepository.findById(request.orderListId())
			.orElseThrow(() -> new BookNotFoundException(request.orderListId()));

		if (reviewRepository.existsByBookOrderOrderListId(request.orderListId())) {
			throw new ReviewAlreadyExistsException(request.orderListId());
		}

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		Review review = reviewRepository.save(Review.toEntity(request, bookOrder, user));

		// 파일 이름이 비어있지 않으면 이미지 저장
		if (request.fileName() != null) {
			Image image = imageRepository.save(
				new Image(ImageUtil.fileNameParser(request.fileName()), request.fileName()));
			reviewImageRepository.save(ReviewImage.toEntity(review, image));
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public GetReviewResponse getReview(Long reviewId) {
		return reviewRepository.getReview(reviewId).orElseThrow(() -> new ReviewNotFoundException(reviewId));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateReview(Long reviewId, UpdateReviewRequest request, CurrentUserDetails currentUser) {
		Long userId = currentUser != null ? currentUser.getUserId() : null;

		Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException(reviewId));
		Long requestId = review.getUser().getId();

		if (!requestId.equals(userId)) {
			throw new AccessDeniedException(requestId, userId);
		}

		// 파일 이름이 비어있지 않으면 이미지 저장
		if (request.fileName() != null) {
			reviewImageRepository.deleteAllByReview_ReviewId(reviewId);
			Image image = imageRepository.save(
				new Image(ImageUtil.fileNameParser(request.fileName()), request.fileName()));
			reviewImageRepository.save(ReviewImage.toEntity(review, image));
		}

		review.updateReviewScore(request.reviewScore(), request.reviewComment());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteReview(Long reviewId) {
		reviewImageRepository.deleteAllByReview_ReviewId(reviewId);
		reviewRepository.deleteById(reviewId);
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public double getReviewsAverageScoreByBookId(Long bookId) {
		return reviewRepository.getReviewsAverageScoreByBookId(bookId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GetBookOrderWithoutReviewResponse> getBooksWithoutReviewsByUserId(CurrentUserDetails currentUser) {
		Long userId = currentUser != null ? currentUser.getUserId() : null;
		return reviewRepository.getBooksWithoutReviewsByUserId(userId);
	}

}
