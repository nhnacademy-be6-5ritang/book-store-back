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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 저자 관련 HTTP 요청을 처리하는 컨트롤러입니다.
 * 이 컨트롤러는 저자의 CRUD 기능을 제공합니다.
 *
 * @version 1.0
 */
@Tag(name = "Author", description = "저자 관련 API")
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
	@Operation(
		summary = "저자 리스트 조회",
		description = "모든 저자 리스트를 조회 합니다"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "모든 저자 리스트가 성공적으로 조회 되었습니다."),
	})
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
	@Operation(
		summary = "저자 페이지 조회",
		description = "모든 저자 페이지를 조회 합니다"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "모든 저자 페이지가 성공적으로 조회 되었습니다."),
	})
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
	@Operation(
		summary = "저자 조회",
		description = "특정 저자를 조회 합니다"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "특정 저자가 성공적으로 조회 되었습니다."),
		@ApiResponse(responseCode = "404", description = "요청된 저자를 찾을 수 없습니다.")
	})
	@GetMapping("/{authorId}")
	public ResponseEntity<AuthorDto> getAuthor(@PathVariable Long authorId) {
		return ResponseEntity.status(HttpStatus.OK).body(authorService.getAuthor(authorId));
	}

	/**
	 * 새로운 저자를 생성합니다.
	 *
	 * @param request 생성할 저자 정보 DTO
	 * @return 응답 상태 코드 (201 CREATED)
	 */
	@Operation(
		summary = "저자 생성",
		description = "새로운 저자를 생성합니다"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "저자가 성공적으로 생성되었습니다."),
		@ApiResponse(responseCode = "400", description = "잘못된 저자 데이터입니다."),
		@ApiResponse(responseCode = "409", description = "이미 존재하는 저자 입니다.")
	})
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
	@Operation(
		summary = "저자 수정",
		description = "저자를 수정 합니다"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "새로운 저자가 성공적으로 생성되었습니다."),
		@ApiResponse(responseCode = "400", description = "잘못된 저자 데이터입니다."),
		@ApiResponse(responseCode = "404", description = "요청한 저자 정보를 찾을 수 없습니다."),
		@ApiResponse(responseCode = "409", description = "요청한 저자가 이미 존재합니다.")
	})
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
	@Operation(
		summary = "저자 삭제",
		description = "저자를 삭제 합니다"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "저자가 성공적으로 삭제되었습니다."),
		@ApiResponse(responseCode = "404", description = "요청한 저자 정보를 찾을 수 없습니다.")
	})
	@AuthorizeRole({"BOOK_ADMIN", "HEAD_ADMIN"})
	@DeleteMapping("/{authorId}")
	public ResponseEntity<Void> deleteAuthor(@PathVariable Long authorId) {
		authorService.deleteAuthor(authorId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
