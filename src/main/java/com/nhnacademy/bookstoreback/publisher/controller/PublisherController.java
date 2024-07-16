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

import com.nhnacademy.bookstoreback.publisher.domain.dto.respnse.PublisherDto;
import com.nhnacademy.bookstoreback.publisher.service.impl.PublisherServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 출판사 관리 HTTP 요청을 처리하는 컨트롤러입니다.
 * 이 컨트롤러는 출판사의 CRUD 기능을 제공합니다.
 *
 * @version 1.0
 */
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
	@PostMapping
	public ResponseEntity<PublisherDto> createPublisher(@Valid @RequestBody PublisherDto request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(publisherService.createPublisher(request));
	}

	/**
	 * 주어진 출판사 ID에 해당하는 출판사 정보를 업데이트합니다.
	 *
	 * @param publisherId 업데이트할 출판사 ID
	 * @param request 업데이트할 출판사 정보 DTO
	 * @return 업데이트된 출판사 정보
	 */
	@PutMapping("/{publisherId}")
	public ResponseEntity<PublisherDto> updatePublisher(@PathVariable Long publisherId,
		@Valid @RequestBody PublisherDto request) {
		return ResponseEntity.status(HttpStatus.OK).body(publisherService.updatePublisher(publisherId, request));
	}

	/**
	 * 주어진 출판사 ID에 해당하는 출판사를 삭제합니다.
	 *
	 * @param publisherId 삭제할 출판사 ID
	 * @return 응답 상태 코드 (204 NO CONTENT)
	 */
	@DeleteMapping("/{publisherId}")
	public ResponseEntity<Void> deletePublisher(@PathVariable Long publisherId) {
		publisherService.deletePublisher(publisherId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
