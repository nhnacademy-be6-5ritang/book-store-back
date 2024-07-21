package com.nhnacademy.bookstoreback.author.controller;

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
import com.nhnacademy.bookstoreback.author.domain.dto.respnse.AuthorDto;
import com.nhnacademy.bookstoreback.author.service.impl.AuthorServiceImpl;

import jakarta.validation.Valid;
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
	public ResponseEntity<Page<AuthorDto>> getAuthors(@PageableDefault(page = 1, size = 10) Pageable pageable) {
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
	 * @return 응답 상태 코드 (204 CREATED)
	 */
	@AuthorizeRole({"BOOK_ADMIN", "HEAD_ADMIN"})
	@PostMapping
	public ResponseEntity<Void> createAuthor(
		@Valid @RequestBody AuthorDto request) {
		authorService.createAuthor(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * 주어진 저자 ID에 해당하는 저자 정보를 업데이트합니다.
	 *
	 * @param authorId 저자 ID
	 * @param request  업데이트할 저자 정보 DTO
	 * @return 응답 상태 코드 (200 OK)
	 */
	@AuthorizeRole({"BOOK_ADMIN", "HEAD_ADMIN"})
	@PutMapping("/{authorId}")
	public ResponseEntity<Void> updateAuthor(@PathVariable Long authorId,
		@Valid @RequestBody AuthorDto request) {
		authorService.updateAuthor(authorId, request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	/**
	 * 주어진 저자 ID에 해당하는 저자를 삭제합니다.
	 *
	 * @param authorId 삭제할 저자 ID
	 * @return 응답 상태 코드 (200 OK)
	 */
	@AuthorizeRole({"BOOK_ADMIN", "HEAD_ADMIN"})
	@DeleteMapping("/{authorId}")
	public ResponseEntity<Void> deleteAuthor(@PathVariable Long authorId) {
		authorService.deleteAuthor(authorId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
