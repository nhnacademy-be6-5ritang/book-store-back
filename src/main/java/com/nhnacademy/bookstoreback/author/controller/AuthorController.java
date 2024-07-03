package com.nhnacademy.bookstoreback.author.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import com.nhnacademy.bookstoreback.author.domain.dto.respnse.AuthorDto;
import com.nhnacademy.bookstoreback.author.service.impl.AuthorServiceImpl;

import lombok.RequiredArgsConstructor;

/**
 * 저자 관련 HTTP 요청을 처리하는 컨트롤러입니다.
 * 이 컨트롤러는 저자의 CRUD 기능을 제공합니다.
 *
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authors")
public class AuthorController {
	private final AuthorServiceImpl authorService;

	/**
	 * 모든 저자 정보를 조회합니다.
	 *
	 * @return 모든 저자 정보 리스트
	 */
	@GetMapping
	public ResponseEntity<List<AuthorDto>> getAuthors() {
		return ResponseEntity.status(HttpStatus.OK).body(authorService.getAuthors());
	}

	/**
	 * 페이징 처리된 저자 정보를 조회합니다.
	 *
	 * @param pageable 페이지 정보
	 * @return 페이징 처리된 저자 정보 페이지
	 */
	@GetMapping("/page")
	public ResponseEntity<Page<AuthorDto>> getAuthors(Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(authorService.getAuthors(pageable));
	}

	/**
	 * 주어진 저자 ID에 해당하는 저자 정보를 조회합니다.
	 *
	 * @param authorId 저자 ID
	 * @return 해당 저자 정보
	 */
	@GetMapping("/{authorId}")
	public ResponseEntity<AuthorDto> getAuthor(@PathVariable Long authorId) {
		return ResponseEntity.status(HttpStatus.OK).body(authorService.getAuthor(authorId));
	}

	/**
	 * 새로운 저자를 생성합니다.
	 *
	 * @param request 생성할 저자 정보 DTO
	 * @return 생성된 저자 정보
	 */
	@PostMapping
	public ResponseEntity<AuthorDto> createAuthor(
		@RequestBody AuthorDto request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(authorService.createAuthor(request));
	}

	/**
	 * 주어진 저자 ID에 해당하는 저자 정보를 업데이트합니다.
	 *
	 * @param authorId 저자 ID
	 * @param request  업데이트할 저자 정보 DTO
	 * @return 업데이트된 저자 정보
	 */
	@PutMapping("/{authorId}")
	public ResponseEntity<AuthorDto> updateAuthor(@PathVariable Long authorId,
		@RequestBody AuthorDto request) {
		return ResponseEntity.status(HttpStatus.OK).body(authorService.updateAuthor(authorId, request));
	}

	/**
	 * 주어진 저자 ID에 해당하는 저자를 삭제합니다.
	 *
	 * @param authorId 삭제할 저자 ID
	 * @return 응답 상태 코드 (204 NO CONTENT)
	 */
	@DeleteMapping("/{authorId}")
	public ResponseEntity<Void> deleteAuthor(@PathVariable Long authorId) {
		authorService.deleteAuthor(authorId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
