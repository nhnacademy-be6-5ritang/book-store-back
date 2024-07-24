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

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/point-transactions")
public class PointTransactionController {
	private final PointTransactionService pointTransactionService;

	@GetMapping
	public ResponseEntity<Page<GetPointTransactionResponse>> getPointTransactions(
		@CurrentUser CurrentUserDetails currentUser, Pageable pageable) {
		Page<GetPointTransactionResponse> getPointTransactionResponsePage
			= pointTransactionService.getPointTransactions(currentUser, pageable);
		return ResponseEntity.status(HttpStatus.OK).body(getPointTransactionResponsePage);
	}

	@GetMapping("/all")
	public ResponseEntity<Page<GetAllPointTransactionResponse>> getAllPointTransactions(
		Pageable pageable) {
		Page<GetAllPointTransactionResponse> getPointTransactionResponsePage
			= pointTransactionService.getAllPointTransaction(pageable);
		return ResponseEntity.status(HttpStatus.OK).body(getPointTransactionResponsePage);
	}

	@PostMapping("/reviews")
	public ResponseEntity<Void> reviewPointTransaction(
		@CurrentUser CurrentUserDetails currentUser, @RequestParam String reviewType) {
		pointTransactionService.reviewPointTransaction(currentUser, reviewType);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
