package com.nhnacademy.bookstoreback.publisher.controller;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.auth.annotation.AuthorizeRole;
import com.nhnacademy.bookstoreback.publisher.domain.dto.respnse.PublisherDto;
import com.nhnacademy.bookstoreback.publisher.service.impl.PublisherServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author 김기욱, 이경헌
 * 출판사 관리 HTTP 요청을 처리하는 컨트롤러입니다.
 */
@Tag(name = "Publisher", description = "출판사 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/publishers")
public class PublisherController {
	private final PublisherServiceImpl publisherService;

	/**
	 * 모든 출판사 정보를 조회합니다.
	 *
	 * @return 모든 출판사 정보 리스트
	 */
	@Operation(
		summary = "모든 출판사 조회",
		description = "모든 출판사 정보를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "출판사 목록 조회 성공")
	})
	@GetMapping
	public ResponseEntity<List<PublisherDto>> getPublishers() {
		return ResponseEntity.status(HttpStatus.OK).body(publisherService.getPublishers());
	}

	/**
	 * 페이징 처리된 태그 정보를 조회합니다.
	 *
	 * @param pageable 페이지 정보
	 * @return 페이징 처리된 태그 정보 페이지
	 */
	@Operation(
		summary = "페이징 처리된 출판사 조회",
		description = "페이징 처리된 출판사 정보를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "출판사 목록(페이징) 조회 성공")
	})
	@GetMapping("/page")
	public ResponseEntity<Page<PublisherDto>> getPublishers(@PageableDefault(page = 1, size = 10) Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(publisherService.getPublishers(pageable));
	}

	/**
	 * 주어진 출판사 ID에 해당하는 출판사 정보를 조회합니다.
	 *
	 * @param publisherId 출판사 ID
	 * @return 해당 출판사 정보
	 */
	@Operation(
		summary = "특정 출판사 조회",
		description = "주어진 출판사 ID에 대한 출판사 정보를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "출판사 조회 성공"),
		@ApiResponse(responseCode = "404", description = "요청한 출판사를 찾을 수 없음")
	})
	@GetMapping("/{publisherId}")
	public ResponseEntity<PublisherDto> getPublisher(@PathVariable Long publisherId) {
		return ResponseEntity.status(HttpStatus.OK).body(publisherService.getPublisher(publisherId));
	}

	/**
	 * 새로운 출판사를 생성합니다.
	 *
	 * @param request 생성할 출판사 정보 DTO
	 * @return 생성된 출판사 정보
	 */
	@Operation(
		summary = "새로운 출판사 생성",
		description = "새로운 출판사를 생성합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "출판사가 성공적으로 생성됨"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다."),
		@ApiResponse(responseCode = "409", description = "이미 존재하는 출판사입니다.")
	})
	@AuthorizeRole({"BOOK_ADMIN", "HEAD_ADMIN"})
	@PostMapping
	public ResponseEntity<Void> createPublisher(@Valid @RequestBody PublisherDto request) {
		publisherService.createPublisher(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * 주어진 출판사 ID에 해당하는 출판사 정보를 업데이트합니다.
	 *
	 * @param publisherId 업데이트할 출판사 ID
	 * @param request 업데이트할 출판사 정보 DTO
	 * @return 업데이트된 출판사 정보
	 */
	@Operation(
		summary = "출판사 업데이트",
		description = "주어진 출판사 ID에 대한 출판사 정보를 업데이트합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "출판사 정보가 성공적으로 업데이트됨"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다."),
		@ApiResponse(responseCode = "404", description = "요청한 출판사를 찾을 수 없음"),
		@ApiResponse(responseCode = "409", description = "이미 존재하는 출판사입니다.")
	})
	@AuthorizeRole({"BOOK_ADMIN", "HEAD_ADMIN"})
	@PutMapping("/{publisherId}")
	public ResponseEntity<Void> updatePublisher(@PathVariable Long publisherId,
		@Valid @RequestBody PublisherDto request) {
		publisherService.updatePublisher(publisherId, request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	/**
	 * 주어진 출판사 ID에 해당하는 출판사를 삭제합니다.
	 *
	 * @param publisherId 삭제할 출판사 ID
	 * @return 응답 상태 코드 (204 NO CONTENT)
	 */
	@Operation(
		summary = "출판사 삭제",
		description = "주어진 출판사 ID에 대한 출판사를 삭제합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "출판사가 성공적으로 삭제됨")
	})
	@AuthorizeRole({"BOOK_ADMIN", "HEAD_ADMIN"})
	@DeleteMapping("/{publisherId}")
	public ResponseEntity<Void> deletePublisher(@PathVariable Long publisherId) {
		publisherService.deletePublisher(publisherId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
