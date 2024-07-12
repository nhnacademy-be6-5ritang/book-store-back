package com.nhnacademy.bookstoreback.review.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.exception.BookNotFoundException;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.image.domain.entity.Image;
import com.nhnacademy.bookstoreback.image.repository.ImageRepository;
import com.nhnacademy.bookstoreback.review.domain.dto.request.CreateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.request.UpdateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.response.GetReviewResponse;
import com.nhnacademy.bookstoreback.review.domain.entity.Review;
import com.nhnacademy.bookstoreback.review.domain.entity.ReviewImage;
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
	private final ReviewRepository reviewRepository;
	private final BookRepository bookRepository;
	private final UserRepository userRepository;
	private final ImageRepository imageRepository;
	private final ReviewImageRepository reviewImageRepository;

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

		return reviewRepository.findAllByBookBookId(bookId,
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

		return reviewRepository.findAllByBookBookIdAndReviewImagesEmpty(bookId,
				PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "reviewCreatedAt")))
			.map(review -> GetReviewResponse.fromEntity(review, null));
	}

	@Override
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> getPhotoReviewsByBookId(Long bookId, Pageable pageable) {
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();

		return reviewRepository.findAllByBookBookIdAndReviewImagesNotEmpty(bookId,
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

		Book book = bookRepository.findById(request.bookId())
			.orElseThrow(() -> new BookNotFoundException(request.bookId()));

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		Review review = reviewRepository.save(Review.toEntity(request, book, user));

		// 파일 이름이 비어있지 않으면 이미지 저장
		if (request.fileName() != null) {
			Image image = imageRepository.save(new Image(ImageNameParser(request.fileName()), request.fileName()));
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

	@Override
	public double getReviewsAverageScoreByBookId(Long bookId) {
		return reviewRepository.getReviewsAverageScoreByBookId(bookId);
	}

	public static String ImageNameParser(String fileName) {
		// 파일 이름을 "_"로 분리하여 배열로 만듭니다.
		String[] parts = fileName.split("_", 2);

		// parts 배열의 두 번째 요소가 실제 파일 이름이 포함된 부분입니다.
		if (parts.length > 1) {
			String filePart = parts[1];

			// 파일 이름에서 마지막 "."의 위치를 찾습니다.
			int lastDotIndex = filePart.lastIndexOf('.');

			// 마지막 "."이 있는 경우
			if (lastDotIndex != -1) {
				// 파일 이름의 확장자를 제외한 부분을 반환합니다.
				return filePart.substring(0, lastDotIndex);
			} else {
				// 마지막 "."이 없는 경우 전체 파일 이름을 반환합니다.
				return filePart;
			}
		}

		// "_"가 없거나 제대로 분리되지 않은 경우 빈 문자열 반환
		return "";
	}

}
