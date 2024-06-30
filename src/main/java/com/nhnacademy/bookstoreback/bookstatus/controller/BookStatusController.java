package com.nhnacademy.bookstoreback.bookstatus.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.bookstatus.domain.dto.respnse.BookStatusDto;
import com.nhnacademy.bookstoreback.bookstatus.service.BookStatusService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookStatuses")
public class BookStatusController {
	private final BookStatusService bookStatusService;

	@GetMapping
	public ResponseEntity<List<BookStatusDto>> getBookStatuses() {
		return ResponseEntity.status(HttpStatus.OK).body(bookStatusService.getBookStatuses());
	}

	@GetMapping("/{bookStatusId}")
	public ResponseEntity<BookStatusDto> getBookStatus(@PathVariable Long bookStatusId) {
		return ResponseEntity.status(HttpStatus.OK).body(bookStatusService.getBookStatus(bookStatusId));
	}

	@PostMapping
	public ResponseEntity<BookStatusDto> createBookStatus(
		@RequestBody BookStatusDto request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(bookStatusService.createBookStatus(request));
	}

	@PutMapping("/{bookStatusId}")
	public ResponseEntity<BookStatusDto> updateBookStatus(@PathVariable Long bookStatusId,
		@RequestBody BookStatusDto request) {
		return ResponseEntity.status(HttpStatus.OK).body(bookStatusService.updateBookStatus(bookStatusId, request));
	}

	@DeleteMapping("/{bookStatusId}")
	public ResponseEntity<Void> deleteBookStatus(@PathVariable Long bookStatusId) {
		bookStatusService.deleteBookStatus(bookStatusId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
