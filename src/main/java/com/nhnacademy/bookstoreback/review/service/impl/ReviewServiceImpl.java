package com.nhnacademy.bookstoreback.review.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.exception.BookNotFoundException;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.image.domain.entity.Image;
import com.nhnacademy.bookstoreback.image.repository.ImageRepository;
import com.nhnacademy.bookstoreback.review.domain.dto.request.CreateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.request.UpdateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.response.GetReviewResponse;
import com.nhnacademy.bookstoreback.review.domain.entity.Review;
import com.nhnacademy.bookstoreback.review.exception.ReviewNotFoundException;
import com.nhnacademy.bookstoreback.review.repository.ReviewRepository;
import com.nhnacademy.bookstoreback.review.service.ReviewService;
import com.nhnacademy.bookstoreback.reviewimage.domain.entity.ReviewImage;
import com.nhnacademy.bookstoreback.reviewimage.repository.ReviewImageRepository;
import com.nhnacademy.bookstoreback.reviewimage.service.impl.CloudStorageService;
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
	private final ReviewRepository reviewRepository;
	private final BookRepository bookRepository;
	private final UserRepository userRepository;
	private final ImageRepository imageRepository;
	private final ReviewImageRepository reviewImageRepository;
	private final CloudStorageService cloudStorageService;  // 클라우드 저장소 서비스 (구현 필요)

	/**
	 * 모든 리뷰를 페이지네이션하여 조회합니다.
	 *
	 * @param pageable 페이지네이션 정보
	 * @return 페이지네이션된 리뷰 응답
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> findAllReviews(Pageable pageable) {
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();

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
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();

		Page<Review> reviews = reviewRepository.findAllByBookBookId(bookId,
			PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "reviewCreatedAt")));

		return reviews
			.map(GetReviewResponse::fromEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> findReviewsByUserId(Pageable pageable, CurrentUserDetails currentUser) {
		Long userId = currentUser != null ? currentUser.getUserId() : null;
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();

		Page<Review> reviews = reviewRepository.findAllByUserId(userId,
			PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "reviewCreatedAt")));

		return reviews
			.map(GetReviewResponse::fromEntity);
	}

	/**
	 * 새로운 리뷰를 저장합니다.
	 *
	 * @param request 리뷰 생성 요청 DTO
	 */
	@Override
	public void saveReview(CreateReviewRequest request, CurrentUserDetails currentUser, MultipartFile image) {
		Long userId = currentUser != null ? currentUser.getUserId() : null;

		Book book = bookRepository.findById(request.bookId())
			.orElseThrow(() -> new BookNotFoundException(request.bookId()));

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		Review review = reviewRepository.save(Review.toEntity(request, book, user));

		if (image != null && !image.isEmpty()) {
			String imageUrl = null;
			try {
				imageUrl = cloudStorageService.uploadFile(image);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			Image image1 = imageRepository.save(new Image(review.getReviewId().toString(), imageUrl));

			ReviewImage reviewImage = new ReviewImage();
			reviewImage.setReview(review);
			reviewImage.setImage(image1);
			reviewImageRepository.save(reviewImage);
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
	public void updateReview(Long reviewId, UpdateReviewRequest request) {
		Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException(reviewId));
		review.updateReviewScore(request.reviewScore(), request.reviewComment());
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
