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

import com.nhnacademy.bookstoreback.auth.annotation.AuthorizeRole;
import com.nhnacademy.bookstoreback.bookstatus.domain.dto.respnse.BookStatusDto;
import com.nhnacademy.bookstoreback.bookstatus.service.impl.BookStatusServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author 김기욱, 이경헌
 * 도서 상태 관리 HTTP 요청을 처리하는 컨트롤러입니다.
 */
@Tag(name = "BookStatus", description = "도서상태 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookStatuses")
public class BookStatusController {
	private final BookStatusServiceImpl bookStatusService;

	/**
	 * 모든 도서 상태 정보를 조회합니다.
	 *
	 * @return 모든 도서 상태 정보 리스트
	 */
	@Operation(
		summary = "도서 상태 목록 조회",
		description = "모든 도서 상태 정보를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "도서 상태 목록 조회 성공")
	})
	@AuthorizeRole({"BOOK_ADMIN", "HEAD_ADMIN"})
	@GetMapping
	public ResponseEntity<List<BookStatusDto>> getBookStatuses() {
		return ResponseEntity.status(HttpStatus.OK).body(bookStatusService.getBookStatuses());
	}

	/**
	 * 주어진 도서 상태 ID에 해당하는 도서 상태 정보를 조회합니다.
	 *
	 * @param bookStatusId 도서 상태 ID
	 * @return 해당 도서 상태 정보
	 */
	@Operation(
		summary = "도서 상태 조회",
		description = "주어진 도서 상태 ID에 해당하는 도서 상태 정보를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "도서 상태 조회 성공"),
		@ApiResponse(responseCode = "404", description = "해당 도서 상태를 찾을 수 없습니다.")
	})
	@AuthorizeRole({"BOOK_ADMIN", "HEAD_ADMIN"})
	@GetMapping("/{bookStatusId}")
	public ResponseEntity<BookStatusDto> getBookStatus(@PathVariable Long bookStatusId) {
		return ResponseEntity.status(HttpStatus.OK).body(bookStatusService.getBookStatus(bookStatusId));
	}

	/**
	 * 새로운 도서 상태를 생성합니다.
	 *
	 * @param request 생성할 도서 상태 정보 DTO
	 * @return 응답 상태 코드 (201 CREATED)
	 */
	@Operation(
		summary = "도서 상태 생성",
		description = "새로운 도서 상태를 생성합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "도서 상태 생성 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다."),
		@ApiResponse(responseCode = "409", description = "이미 존재하는 도서 상태입니다.")
	})
	@AuthorizeRole({"BOOK_ADMIN", "HEAD_ADMIN"})
	@PostMapping
	public ResponseEntity<Void> createBookStatus(
		@Valid @RequestBody BookStatusDto request) {
		bookStatusService.createBookStatus(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * 주어진 도서 상태 ID에 해당하는 도서 상태 정보를 업데이트합니다.
	 *
	 * @param bookStatusId 도서 상태 ID
	 * @param request 업데이트할 도서 상태 정보 DTO
	 * @return 응답 상태 코드 (200 OK)
	 */
	@Operation(
		summary = "도서 상태 업데이트",
		description = "주어진 도서 상태 ID에 해당하는 도서 상태 정보를 업데이트합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "도서 상태 업데이트 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다."),
		@ApiResponse(responseCode = "404", description = "해당 도서 상태를 찾을 수 없습니다."),
		@ApiResponse(responseCode = "409", description = "이미 존재하는 도서 상태입니다.")
	})
	@AuthorizeRole({"BOOK_ADMIN", "HEAD_ADMIN"})
	@PutMapping("/{bookStatusId}")
	public ResponseEntity<Void> updateBookStatus(@PathVariable Long bookStatusId,
		@Valid @RequestBody BookStatusDto request) {
		bookStatusService.updateBookStatus(bookStatusId, request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	/**
	 * 주어진 도서 상태 ID에 해당하는 도서 상태를 삭제합니다.
	 *
	 * @param bookStatusId 삭제할 도서 상태 ID
	 * @return 응답 상태 코드 (200 OK)
	 */
	@Operation(
		summary = "도서 상태 삭제",
		description = "주어진 도서 상태 ID에 해당하는 도서 상태를 삭제합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "도서 상태 삭제 성공")
	})
	@AuthorizeRole({"BOOK_ADMIN", "HEAD_ADMIN"})
	@DeleteMapping("/{bookStatusId}")
	public ResponseEntity<Void> deleteBookStatus(@PathVariable Long bookStatusId) {
		bookStatusService.deleteBookStatus(bookStatusId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
