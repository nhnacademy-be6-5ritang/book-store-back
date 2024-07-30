package com.nhnacademy.bookstoreback.point.transaction.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.point.transaction.domain.dto.response.GetAllPointTransactionResponse;
import com.nhnacademy.bookstoreback.point.transaction.domain.dto.response.GetPointTransactionResponse;
import com.nhnacademy.bookstoreback.point.transaction.service.PointTransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @author 김태환
 * 포인트 거래 관련 API를 제공하는 컨트롤러입니다.
 */
@Tag(name = "Point Transaction", description = "포인트 거래 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/point-transactions")
public class PointTransactionController {
	private final PointTransactionService pointTransactionService;

	/**
	 * 현재 사용자의 포인트 거래 내역을 페이징하여 조회합니다.
	 *
	 * @param currentUser 현재 사용자의 상세 정보
	 * @param pageable 페이징 정보
	 * @return 현재 사용자의 포인트 거래 내역
	 */
	@Operation(
		summary = "포인트 거래 내역 조회",
		description = "현재 사용자의 포인트 거래 내역을 페이징하여 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "포인트 거래 내역이 성공적으로 반환되었습니다."),
		@ApiResponse(responseCode = "404", description = "포인트 거래 내역을 찾을 수 없습니다.")
	})
	@GetMapping
	public ResponseEntity<Page<GetPointTransactionResponse>> getPointTransactions(
		@CurrentUser CurrentUserDetails currentUser, Pageable pageable) {
		Page<GetPointTransactionResponse> getPointTransactionResponsePage
			= pointTransactionService.getPointTransactions(currentUser, pageable);
		return ResponseEntity.status(HttpStatus.OK).body(getPointTransactionResponsePage);
	}

	/**
	 * 모든 포인트 거래 내역을 페이징하여 조회합니다. (관리자용)
	 *
	 * @param pageable 페이징 정보
	 * @return 모든 포인트 거래 내역
	 */
	@Operation(
		summary = "모든 포인트 거래 내역 조회",
		description = "모든 포인트 거래 내역을 페이징하여 조회합니다. (관리자용)"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "모든 포인트 거래 내역이 성공적으로 반환되었습니다."),
		@ApiResponse(responseCode = "404", description = "포인트 거래 내역을 찾을 수 없습니다.")
	})
	@GetMapping("/all")
	public ResponseEntity<Page<GetAllPointTransactionResponse>> getAllPointTransactions(
		Pageable pageable) {
		Page<GetAllPointTransactionResponse> getPointTransactionResponsePage
			= pointTransactionService.getAllPointTransaction(pageable);
		return ResponseEntity.status(HttpStatus.OK).body(getPointTransactionResponsePage);
	}

	/**
	 * 현재 사용자의 리뷰에 따라 포인트 거래를 생성합니다.
	 *
	 * @param currentUser 현재 사용자의 상세 정보
	 * @param reviewType 리뷰 타입 (포인트 적립 정책을 결정합니다.)
	 */
	@Operation(
		summary = "리뷰에 따른 포인트 거래 생성",
		description = "현재 사용자의 리뷰에 따라 포인트 거래를 생성합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "포인트 거래가 성공적으로 생성되었습니다."),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다."),
		@ApiResponse(responseCode = "404", description = "리뷰 타입이 유효하지 않습니다."),
		@ApiResponse(responseCode = "409", description = "포인트 거래 처리 중 충돌이 발생했습니다.")
	})
	@PostMapping("/reviews")
	public ResponseEntity<Void> reviewPointTransaction(
		@CurrentUser CurrentUserDetails currentUser, @RequestParam String reviewType) {
		pointTransactionService.reviewPointTransaction(currentUser, reviewType);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
