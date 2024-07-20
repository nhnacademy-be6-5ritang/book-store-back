package com.nhnacademy.bookstoreback.tag.controller;

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
import com.nhnacademy.bookstoreback.tag.domain.dto.respnse.TagDto;
import com.nhnacademy.bookstoreback.tag.service.impl.TagServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 태그 관리 HTTP 요청을 처리하는 컨트롤러입니다.
 * 이 컨트롤러는 태그의 CRUD 기능을 제공합니다.
 *
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TagController {
	private final TagServiceImpl tagService;

	/**
	 * 모든 태그 정보를 조회합니다.
	 *
	 * @return 모든 태그 정보 리스트
	 */
	@GetMapping("/tags")
	public ResponseEntity<List<TagDto>> getTags() {
		return ResponseEntity.status(HttpStatus.OK).body(tagService.getTags());
	}

	/**
	 * 페이징 처리된 태그 정보를 조회합니다.
	 *
	 * @param pageable 페이지 정보
	 * @return 페이징 처리된 태그 정보 페이지
	 */
	@GetMapping("/tags/page")
	public ResponseEntity<Page<TagDto>> getTags(@PageableDefault(page = 1, size = 10) Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(tagService.getTags(pageable));
	}

	/**
	 * 주어진 책 ID에 해당하는 태그 정보를 조회합니다.
	 *
	 * @param bookId 책 ID
	 * @return 해당 책에 속한 태그 정보 리스트
	 */
	@GetMapping("/books/{bookId}/tags")
	public ResponseEntity<List<TagDto>> getTagsByTagId(@PathVariable Long bookId) {
		return ResponseEntity.status(HttpStatus.OK).body(tagService.getTagsByTagId(bookId));
	}

	/**
	 * 주어진 태그 ID에 해당하는 태그 정보를 조회합니다.
	 *
	 * @param tagId 태그 ID
	 * @return 해당 태그 정보
	 */
	@GetMapping("/tags/{tagId}")
	public ResponseEntity<TagDto> getTag(@PathVariable Long tagId) {
		return ResponseEntity.status(HttpStatus.OK).body(tagService.getTag(tagId));
	}

	/**
	 * 새로운 태그를 생성합니다.
	 *
	 * @param request 생성할 태그 정보 DTO
	 * @return 생성된 태그 정보
	 */
	@AuthorizeRole({"BOOK_ADMIN", "HEAD_ADMIN"})
	@PostMapping("/tags")
	public ResponseEntity<TagDto> createTag(
		@Valid @RequestBody TagDto request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(tagService.createTag(request));
	}

	/**
	 * 주어진 태그 ID에 해당하는 태그 정보를 업데이트합니다.
	 *
	 * @param tagId   업데이트할 태그 ID
	 * @param request 업데이트할 태그 정보 DTO
	 * @return 업데이트된 태그 정보
	 */
	@AuthorizeRole({"BOOK_ADMIN", "HEAD_ADMIN"})
	@PutMapping("/tags/{tagId}")
	public ResponseEntity<TagDto> updateTag(@PathVariable Long tagId,
		@Valid @RequestBody TagDto request) {
		return ResponseEntity.status(HttpStatus.OK).body(tagService.updateTag(tagId, request));
	}

	/**
	 * 주어진 태그 ID에 해당하는 태그를 삭제합니다.
	 *
	 * @param tagId 삭제할 태그 ID
	 * @return 응답 상태 코드 (204 NO CONTENT)
	 */
	@AuthorizeRole({"BOOK_ADMIN", "HEAD_ADMIN"})
	@DeleteMapping("/tags/{tagId}")
	public ResponseEntity<Void> deleteTag(@PathVariable Long tagId) {
		tagService.deleteTag(tagId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
