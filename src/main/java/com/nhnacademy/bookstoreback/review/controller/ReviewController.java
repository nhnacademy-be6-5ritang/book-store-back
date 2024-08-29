package com.nhnacademy.bookstoreback.review.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.auth.annotation.AuthorizeRole;
import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.review.domain.dto.request.CreateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.request.UpdateReviewRequest;
import com.nhnacademy.bookstoreback.review.domain.dto.response.GetBookOrderWithoutReviewResponse;
import com.nhnacademy.bookstoreback.review.domain.dto.response.GetReviewResponse;
import com.nhnacademy.bookstoreback.review.service.ReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author 이경헌
 * 리뷰와 관련된 API 요청을 처리하는 컨트롤러입니다.
 */
@Tag(name = "Review", description = "리뷰 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {
	private final ReviewService reviewService;

	/**
	 * 모든 리뷰를 페이지네이션하여 조회합니다.
	 *
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 페이지네이션된 모든 리뷰 목록 (Page 객체)
	 */
	@Operation(
		summary = "모든 리뷰 조회",
		description = "모든 리뷰를 페이지네이션하여 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "모든 리뷰 조회 성공")
	})
	@GetMapping("/reviews/all")
	public ResponseEntity<Page<GetReviewResponse>> getReviews(@PageableDefault(page = 1, size = 5) Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviews(pageable));
	}

	/**
	 * 모든 사진 리뷰를 페이지네이션하여 조회합니다.
	 *
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 페이지네이션된 모든 리뷰 목록 (Page 객체)
	 */
	@Operation(
		summary = "모든 사진 리뷰 조회",
		description = "모든 사진 리뷰를 페이지네이션하여 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "모든 사진 리뷰 조회 성공")
	})
	@GetMapping("/reviews/photo")
	public ResponseEntity<Page<GetReviewResponse>> getPhotoReviews(
		@PageableDefault(page = 1, size = 5) Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(reviewService.getPhotoReviews(pageable));
	}

	/**
	 * 모든 일반 리뷰를 페이지네이션하여 조회합니다.
	 *
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 페이지네이션된 모든 리뷰 목록 (Page 객체)
	 */
	@Operation(
		summary = "모든 일반 리뷰 조회",
		description = "모든 일반 리뷰를 페이지네이션하여 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "모든 일반 리뷰 조회 성공")
	})
	@GetMapping("/reviews/general")
	public ResponseEntity<Page<GetReviewResponse>> getGeneralReviews(
		@PageableDefault(page = 1, size = 5) Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(reviewService.getGeneralReviews(pageable));
	}

	/**
	 * 특정 책의 모든 리뷰를 페이지네이션하여 조회합니다.
	 *
	 * @param bookId 책의 ID
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 페이지네이션된 특정 책의 리뷰 목록 (Page 객체)
	 */
	@Operation(
		summary = "특정 책의 모든 리뷰 조회",
		description = "특정 책의 모든 리뷰를 페이지네이션하여 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "특정 책의 모든 리뷰 조회 성공"),
		@ApiResponse(responseCode = "404", description = "책을 찾을 수 없음")
	})
	@GetMapping("/books/{bookId}/reviews/all")
	public ResponseEntity<Page<GetReviewResponse>> getReviewsByBookId(
		@PageableDefault(page = 1, size = 5) Pageable pageable,
		@PathVariable Long bookId) {
		Page<GetReviewResponse> reviews = reviewService.getReviewsByBookId(bookId, pageable);
		return ResponseEntity.status(HttpStatus.OK).body(reviews);
	}

	/**
	 * 특정 책의 사진 리뷰를 페이지네이션하여 조회합니다.
	 *
	 * @param bookId 책의 ID
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 페이지네이션된 특정 책의 사진 리뷰 목록 (Page 객체)
	 */
	@Operation(
		summary = "특정 책의 사진 리뷰 조회",
		description = "특정 책의 사진 리뷰를 페이지네이션하여 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "특정 책의 사진 리뷰 조회 성공"),
		@ApiResponse(responseCode = "404", description = "책을 찾을 수 없음")
	})
	@GetMapping("/books/{bookId}/reviews/photo")
	public ResponseEntity<Page<GetReviewResponse>> getPhotoReviewsByBookId(
		@PageableDefault(page = 1, size = 5) Pageable pageable,
		@PathVariable Long bookId) {
		Page<GetReviewResponse> reviews = reviewService.getPhotoReviewsByBookId(bookId, pageable);
		return ResponseEntity.status(HttpStatus.OK).body(reviews);
	}

	/**
	 * 특정 책의 일반 리뷰를 페이지네이션하여 조회합니다.
	 *
	 * @param bookId 책의 ID
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @return 페이지네이션된 특정 책의 일반 리뷰 목록 (Page 객체)
	 */
	@Operation(
		summary = "특정 책의 일반 리뷰 조회",
		description = "특정 책의 일반 리뷰를 페이지네이션하여 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "특정 책의 일반 리뷰 조회 성공"),
		@ApiResponse(responseCode = "404", description = "책을 찾을 수 없음")
	})
	@GetMapping("/books/{bookId}/reviews/general")
	public ResponseEntity<Page<GetReviewResponse>> getGeneralReviewsByBookId(
		@PageableDefault(page = 1, size = 5) Pageable pageable,
		@PathVariable Long bookId) {
		Page<GetReviewResponse> reviews = reviewService.getGeneralReviewsByBookId(bookId, pageable);
		return ResponseEntity.status(HttpStatus.OK).body(reviews);
	}

	/**
	 * 특정 사용자의 모든 리뷰를 페이지네이션하여 조회합니다.
	 *
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @param currentUser 현재 사용자의 정보 (사용자 ID 등)
	 * @return 페이지네이션된 특정 사용자의 리뷰 목록 (Page 객체)
	 */
	@Operation(
		summary = "특정 사용자의 모든 리뷰 조회",
		description = "특정 사용자의 모든 리뷰를 페이지네이션하여 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "특정 사용자의 모든 리뷰 조회 성공"),
		@ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
	})
	@AuthorizeRole({"MEMBER", "HEAD_ADMIN"})
	@GetMapping("/users/me/reviews/all")
	public ResponseEntity<Page<GetReviewResponse>> getReviewsByUserId(
		@PageableDefault(page = 1, size = 5) Pageable pageable, @CurrentUser CurrentUserDetails currentUser) {
		Page<GetReviewResponse> reviews = reviewService.getReviewsByUserId(pageable, currentUser);
		return ResponseEntity.status(HttpStatus.OK).body(reviews);
	}

	/**
	 * 특정 사용자의 일반 리뷰를 페이지네이션하여 조회합니다.
	 *
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @param currentUser 현재 사용자의 정보 (사용자 ID 등)
	 * @return 페이지네이션된 특정 사용자의 일반 리뷰 목록 (Page 객체)
	 */
	@Operation(
		summary = "특정 사용자의 일반 리뷰 조회",
		description = "특정 사용자의 일반 리뷰를 페이지네이션하여 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "특정 사용자의 일반 리뷰 조회 성공"),
		@ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
	})
	@AuthorizeRole({"MEMBER", "HEAD_ADMIN"})
	@GetMapping("/users/me/reviews/general")
	public ResponseEntity<Page<GetReviewResponse>> getGeneralReviewsByUserId(
		@PageableDefault(page = 1, size = 5) Pageable pageable, @CurrentUser CurrentUserDetails currentUser) {
		Page<GetReviewResponse> reviews = reviewService.getGeneralReviewsByUserId(pageable, currentUser);
		return ResponseEntity.status(HttpStatus.OK).body(reviews);
	}

	/**
	 * 특정 사용자의 사진 리뷰를 페이지네이션하여 조회합니다.
	 *
	 * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기 등)
	 * @param currentUser 현재 사용자의 정보 (사용자 ID 등)
	 * @return 페이지네이션된 특정 사용자의 사진 리뷰 목록 (Page 객체)
	 */
	@Operation(
		summary = "특정 사용자의 사진 리뷰 조회",
		description = "특정 사용자의 사진 리뷰를 페이지네이션하여 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "특정 사용자의 사진 리뷰 조회 성공"),
		@ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
	})
	@AuthorizeRole({"MEMBER", "HEAD_ADMIN"})
	@GetMapping("/users/me/reviews/photo")
	public ResponseEntity<Page<GetReviewResponse>> getPhotoReviewsByUserId(
		@PageableDefault(page = 1, size = 5) Pageable pageable, @CurrentUser CurrentUserDetails currentUser) {
		Page<GetReviewResponse> reviews = reviewService.getPhotoReviewsByUserId(pageable, currentUser);
		return ResponseEntity.status(HttpStatus.OK).body(reviews);
	}

	/**
	 * 새로운 리뷰를 생성합니다.
	 *
	 * @param request 새로 생성할 리뷰의 정보 (작성자 ID, 책 ID, 평점, 코멘트 등)
	 * @param currentUser 현재 사용자의 정보 (사용자 ID 등)
	 * @return 리뷰 생성 결과 (HTTP 상태 코드)
	 */
	@Operation(
		summary = "새로운 리뷰 생성",
		description = "새로운 리뷰를 생성합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "리뷰가 성공적으로 생성됨"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다."),
		@ApiResponse(responseCode = "404", description = "도서 또는 사용자를 찾을 수 없음"),
		@ApiResponse(responseCode = "409", description = "리뷰가 이미 존재함")
	})
	@AuthorizeRole({"MEMBER", "HEAD_ADMIN"})
	@PostMapping("/reviews")
	public ResponseEntity<Void> createReview(
		@Valid @RequestBody CreateReviewRequest request,
		@CurrentUser CurrentUserDetails currentUser) {
		reviewService.createReview(request, currentUser);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * 특정 리뷰를 조회합니다.
	 *
	 * @param reviewId 리뷰 ID
	 * @return 조회된 리뷰의 정보 (작성자 ID, 책 ID, 평점, 코멘트 등)
	 */
	@Operation(
		summary = "특정 리뷰 조회",
		description = "특정 리뷰를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "리뷰 조회 성공"),
		@ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없음")
	})
	@GetMapping("/reviews/{reviewId}")
	public ResponseEntity<GetReviewResponse> getReview(@PathVariable Long reviewId) {
		return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReview(reviewId));
	}

	/**
	 * 특정 리뷰를 수정합니다.
	 *
	 * @param request 수정할 리뷰의 정보 (수정할 평점, 코멘트 등)
	 * @param reviewId 리뷰 ID
	 * @return 리뷰 수정 결과 (HTTP 상태 코드)
	 */
	@Operation(
		summary = "특정 리뷰 수정",
		description = "특정 리뷰를 수정합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "리뷰 수정 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다."),
		@ApiResponse(responseCode = "403", description = "접근 권한이 없음"),
		@ApiResponse(responseCode = "404", description = "리뷰 또는 사용자를 찾을 수 없음"),
		@ApiResponse(responseCode = "409", description = "리뷰 수정 중 충돌 발생")
	})
	@AuthorizeRole({"MEMBER", "HEAD_ADMIN"})
	@PostMapping("/reviews/{reviewId}")
	public ResponseEntity<Void> updateReview(@Valid @RequestBody UpdateReviewRequest request,
		@PathVariable Long reviewId, @CurrentUser CurrentUserDetails currentUser) {
		reviewService.updateReview(reviewId, request, currentUser);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	/**
	 * 특정 리뷰를 삭제합니다.
	 *
	 * @param reviewId 리뷰 ID
	 * @return 리뷰 삭제 결과 (HTTP 상태 코드)
	 */
	@Operation(
		summary = "특정 리뷰 삭제",
		description = "특정 리뷰를 삭제합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "리뷰 삭제 성공"),
	})
	@AuthorizeRole({"MEMBER", "HEAD_ADMIN"})
	@DeleteMapping("/reviews/{reviewId}")
	public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
		reviewService.deleteReview(reviewId);
		return ResponseEntity.ok().build();
	}

	/**
	 * 특정 책의 리뷰 평균 점수를 조회합니다.
	 *
	 * @param bookId 책의 ID
	 * @return 책의 리뷰 평균 점수
	 */
	@Operation(
		summary = "특정 책의 리뷰 평균 점수 조회",
		description = "특정 책의 리뷰 평균 점수를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "리뷰 평균 점수 조회 성공"),
		@ApiResponse(responseCode = "404", description = "책을 찾을 수 없음")
	})
	@GetMapping("/books/{bookId}/reviews/average")
	public ResponseEntity<Double> getReviewsAverageScoreByBookId(@PathVariable Long bookId) {
		return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviewsAverageScoreByBookId(bookId));
	}

	/**
	 * 현재 사용자가 완료한 주문에 따라 해당 사용자가 리뷰할 수 있는 책 목록을 조회합니다.
	 *
	 * @param currentUser 현재 사용자의 정보 (사용자 ID 등)
	 * @return 사용자가 리뷰할 수 있는 책 목록
	 */
	@Operation(
		summary = "리뷰 작성 가능한 책 목록 조회",
		description = "현재 사용자가 완료한 주문에 따라 해당 사용자가 리뷰할 수 있는 책 목록을 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "리뷰 작성 가능한 책 목록 조회 성공"),
		@ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
	})
	@AuthorizeRole({"MEMBER", "HEAD_ADMIN"})
	@GetMapping("/reviews/create/possible")
	ResponseEntity<List<GetBookOrderWithoutReviewResponse>> getBooksWithoutReviewsByUserId(
		@CurrentUser CurrentUserDetails currentUser) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(reviewService.getBooksWithoutReviewsByUserId(currentUser));
	}
}
