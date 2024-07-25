package com.nhnacademy.bookstoreback.review.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

/**
 * @author
 * 리뷰 서비스 구현체 클래스입니다.
 */
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
	 * 모든 리뷰를 페이지네이션하여 조회합니다.
	 *
	 * @param pageable 페이지네이션 정보
	 * @return 페이지네이션된 리뷰 응답
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> getReviews(Pageable pageable) {
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();

		return reviewRepository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "reviewCreatedAt")))
			.map(review -> {
				ReviewImage reviewImage = reviewImageRepository.findByReviewReviewId(review.getReviewId());
				return GetReviewResponse.fromEntity(review, reviewImage);
			});
	}

	/**
	 * 모든 사진 리뷰를 페이지네이션하여 조회합니다.
	 *
	 * @param pageable 페이지네이션 정보
	 * @return 페이지네이션된 리뷰 응답
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> getPhotoReviews(Pageable pageable) {
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();

		return reviewRepository.findAllByReviewImagesNotEmpty(
				PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "reviewCreatedAt")))
			.map(review -> {
				ReviewImage reviewImage = reviewImageRepository.findByReviewReviewId(review.getReviewId());
				return GetReviewResponse.fromEntity(review, reviewImage);
			});
	}

	/**
	 * 모든 일반 리뷰를 페이지네이션하여 조회합니다.
	 *
	 * @param pageable 페이지네이션 정보
	 * @return 페이지네이션된 리뷰 응답
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> getGeneralReviews(Pageable pageable) {
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();

		return reviewRepository.findAllByReviewImagesEmpty(
				PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "reviewCreatedAt")))
			.map(review -> {
				ReviewImage reviewImage = reviewImageRepository.findByReviewReviewId(review.getReviewId());
				return GetReviewResponse.fromEntity(review, reviewImage);
			});
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
	public Page<GetReviewResponse> getReviewsByBookId(Long bookId, Pageable pageable) {
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();

		return reviewRepository.findAllByBookOrderBookBookId(bookId,
				PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "reviewCreatedAt")))
			.map(review -> {
				ReviewImage reviewImage = reviewImageRepository.findByReviewReviewId(review.getReviewId());
				return GetReviewResponse.fromEntity(review, reviewImage);
			});
	}

	@Override
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> getGeneralReviewsByBookId(Long bookId, Pageable pageable) {
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();

		return reviewRepository.findAllByBookOrderBookBookIdAndReviewImagesEmpty(bookId,
				PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "reviewCreatedAt")))
			.map(review -> GetReviewResponse.fromEntity(review, null));
	}

	@Override
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> getPhotoReviewsByBookId(Long bookId, Pageable pageable) {
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();

		return reviewRepository.findAllByBookOrderBookBookIdAndReviewImagesNotEmpty(bookId,
				PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "reviewCreatedAt")))
			.map(review -> {
				ReviewImage reviewImage = reviewImageRepository.findByReviewReviewId(review.getReviewId());
				return GetReviewResponse.fromEntity(review, reviewImage);
			});
	}

	@Override
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> getReviewsByUserId(Pageable pageable, CurrentUserDetails currentUser) {
		Long userId = currentUser != null ? currentUser.getUserId() : null;
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();

		return reviewRepository.findAllByUserId(userId,
				PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "reviewCreatedAt")))
			.map(review -> {
				ReviewImage reviewImage = reviewImageRepository.findByReviewReviewId(review.getReviewId());
				return GetReviewResponse.fromEntity(review, reviewImage);
			});
	}

	@Override
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> getGeneralReviewsByUserId(Pageable pageable, CurrentUserDetails currentUser) {
		Long userId = currentUser != null ? currentUser.getUserId() : null;
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();

		return reviewRepository.findAllByUserIdAndReviewImagesEmpty(userId,
				PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "reviewCreatedAt")))
			.map(review -> GetReviewResponse.fromEntity(review, null));
	}

	@Override
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> getPhotoReviewsByUserId(Pageable pageable, CurrentUserDetails currentUser) {
		Long userId = currentUser != null ? currentUser.getUserId() : null;
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();

		return reviewRepository.findAllByUserIdAndReviewImagesNotEmpty(userId,
				PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "reviewCreatedAt")))
			.map(review -> {
				ReviewImage reviewImage = reviewImageRepository.findByReviewReviewId(review.getReviewId());
				return GetReviewResponse.fromEntity(review, reviewImage);
			});
	}

	/**
	 * 새로운 리뷰를 저장합니다.
	 *
	 * @param request 리뷰 생성 요청 DTO
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
	 * 리뷰 ID를 기준으로 리뷰를 조회합니다.
	 *
	 * @param reviewId 리뷰의 ID
	 * @return 조회된 리뷰 응답 DTO
	 */
	@Override
	@Transactional(readOnly = true)
	public GetReviewResponse findReviewById(Long reviewId) {
		Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException(reviewId));
		ReviewImage reviewImage = reviewImageRepository.findByReviewReviewId(review.getReviewId());
		return GetReviewResponse.fromEntity(review, reviewImage);
	}

	/**
	 * 리뷰를 업데이트합니다.
	 *
	 * @param reviewId 리뷰의 ID
	 * @param request 리뷰 업데이트 요청 DTO
	 */
	@Override
	public void updateReview(Long reviewId, UpdateReviewRequest request, CurrentUserDetails currentUser) {
		Long userId = currentUser != null ? currentUser.getUserId() : null;

		userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

		Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException(reviewId));
		Long requestId = review.getUser().getId();

		if (!requestId.equals(userId)) {
			throw new AccessDeniedException(requestId, userId);
		}

		// 기존 리뷰 이미지 매핑 제거
		reviewImageRepository.deleteAllByReview_ReviewId(reviewId);

		// 파일 이름이 비어있지 않으면 이미지 저장
		if (request.fileName() != null) {
			Image image = imageRepository.save(
				new Image(ImageUtil.fileNameParser(request.fileName()), request.fileName()));
			reviewImageRepository.save(ReviewImage.toEntity(review, image));
		}
		
		review.updateReviewScore(request.reviewScore(), request.reviewComment());
	}

	/**
	 * 리뷰를 삭제합니다.
	 *
	 * @param reviewId 리뷰의 ID
	 */
	@Override
	public void deleteReview(Long reviewId) {
		reviewImageRepository.deleteAllByReview_ReviewId(reviewId);
		reviewRepository.deleteById(reviewId);
	}

	@Override
	public double getReviewsAverageScoreByBookId(Long bookId) {
		List<Review> reviews = reviewRepository.findAll();

		double sum = 0;
		double count = 0;
		for (Review review : reviews) {
			if (Objects.equals(review.getBookOrder().getBook().getBookId(), bookId)) {
				sum += review.getReviewScore();
				count++;
			}
		}

		if (sum == 0) {
			return 0;
		}

		double averageScore = sum / count;
		return Math.round(averageScore * 100) / 100.0;
	}

	@Override
	public List<GetBookOrderWithoutReviewResponse> getBooksWithoutReviews(CurrentUserDetails currentUser) {
		Long userId = currentUser != null ? currentUser.getUserId() : null;
		List<BookOrder> bookOrders = bookOrderRepository.findAllByOrder_User_IdAndOrder_OrderStatus_OrderStatusName(
			userId, "배송 완료");

		List<Review> reviews = reviewRepository.findAllByUserId(userId);

		Set<Long> reviewedOrderListIds = new HashSet<>();
		for (Review review : reviews) {
			reviewedOrderListIds.add(review.getBookOrder().getOrderListId());
		}

		List<BookOrder> bookOrdersWithoutReviews = new ArrayList<>();
		for (BookOrder bookOrder : bookOrders) {
			if (!reviewedOrderListIds.contains(bookOrder.getOrderListId())) {
				bookOrdersWithoutReviews.add(bookOrder);
			}
		}

		return bookOrdersWithoutReviews.stream().map(GetBookOrderWithoutReviewResponse::fromEntity).toList();
	}

}
