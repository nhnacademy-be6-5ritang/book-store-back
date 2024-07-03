package com.nhnacademy.bookstoreback.category.controller;

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

import com.nhnacademy.bookstoreback.category.domain.dto.request.CreateCategoryRequest;
import com.nhnacademy.bookstoreback.category.domain.dto.request.UpdateCategoryRequest;
import com.nhnacademy.bookstoreback.category.domain.dto.respnse.CreateCategoryResponse;
import com.nhnacademy.bookstoreback.category.domain.dto.respnse.GetCategoryResponse;
import com.nhnacademy.bookstoreback.category.domain.dto.respnse.UpdateCategoryResponse;
import com.nhnacademy.bookstoreback.category.service.impl.CategoryServiceImpl;

import lombok.RequiredArgsConstructor;

/**
 * 카테고리 관리 HTTP 요청을 처리하는 컨트롤러입니다.
 * 이 컨트롤러는 카테고리의 CRUD 기능을 제공합니다.
 *
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
	private final CategoryServiceImpl categoryService;

	/**
	 * 모든 카테고리 정보를 조회합니다.
	 *
	 * @return 모든 카테고리 정보 리스트
	 */
	@GetMapping
	public ResponseEntity<List<GetCategoryResponse>> getCategories() {
		return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategories());
	}

	/**
	 * 페이징된 카테고리 정보를 조회합니다.
	 *
	 * @param pageable 페이지 정보
	 * @return 페이징된 카테고리 정보
	 */
	@GetMapping("/page")
	public ResponseEntity<Page<GetCategoryResponse>> getCategories(Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategories(pageable));
	}

	/**
	 * 주어진 책 ID에 해당하는 카테고리 정보를 조회합니다.
	 *
	 * @param bookId 책 ID
	 * @return 해당 책에 속한 카테고리 리스트
	 */
	@GetMapping("/books/{bookId}")
	public ResponseEntity<List<GetCategoryResponse>> getCategoriesByBookId(@PathVariable Long bookId) {
		return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategoriesByBookId(bookId));
	}

	/**
	 * 주어진 카테고리 ID에 해당하는 카테고리 정보를 조회합니다.
	 *
	 * @param categoryId 카테고리 ID
	 * @return 해당 카테고리 정보
	 */
	@GetMapping("/{categoryId}")
	public ResponseEntity<GetCategoryResponse> getCategory(@PathVariable Long categoryId) {
		return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategory(categoryId));
	}

	/**
	 * 새로운 카테고리를 생성합니다.
	 *
	 * @param request 생성할 카테고리 정보 DTO
	 * @return 생성된 카테고리 정보
	 */
	@PostMapping
	public ResponseEntity<CreateCategoryResponse> createCategory(
		@RequestBody CreateCategoryRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(request));
	}

	/**
	 * 주어진 카테고리 ID에 해당하는 카테고리 정보를 업데이트합니다.
	 *
	 * @param categoryId 업데이트할 카테고리 ID
	 * @param request 업데이트할 카테고리 정보 DTO
	 * @return 업데이트된 카테고리 정보
	 */
	@PutMapping("/{categoryId}")
	public ResponseEntity<UpdateCategoryResponse> updateCategory(@PathVariable Long categoryId,
		@RequestBody UpdateCategoryRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(categoryService.updateCategory(categoryId, request));
	}

	/**
	 * 주어진 카테고리 ID에 해당하는 카테고리를 삭제합니다.
	 *
	 * @param categoryId 삭제할 카테고리 ID
	 * @return 응답 상태 코드 (204 NO CONTENT)
	 */
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
		categoryService.deleteCategory(categoryId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
