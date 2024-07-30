package com.nhnacademy.bookstoreback.category.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.auth.annotation.AuthorizeRole;
import com.nhnacademy.bookstoreback.category.domain.dto.request.CreateCategoryRequest;
import com.nhnacademy.bookstoreback.category.domain.dto.request.UpdateCategoryRequest;
import com.nhnacademy.bookstoreback.category.domain.dto.respnse.CategorySearchResult;
import com.nhnacademy.bookstoreback.category.domain.dto.respnse.GetCategoryResponse;
import com.nhnacademy.bookstoreback.category.service.impl.CategoryServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author 김기욱, 이경헌
 * 카테고리 관리 HTTP 요청을 처리하는 컨트롤러입니다.
 */
@Tag(name = "Category", description = "카테고리 관련 API")
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
	@Operation(
		summary = "모든 카테고리 조회",
		description = "모든 카테고리 정보를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "모든 카테고리 정보 조회 성공")
	})
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
	@Operation(
		summary = "페이징된 카테고리 조회",
		description = "페이징된 카테고리 정보를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "페이징된 카테고리 정보 조회 성공")
	})
	@GetMapping("/page")
	public ResponseEntity<Page<GetCategoryResponse>> getCategories(
		@PageableDefault(page = 1, size = 10) Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategories(pageable));
	}

	/**
	 * 주어진 책 ID에 해당하는 카테고리 정보를 조회합니다.
	 *
	 * @param bookId 책 ID
	 * @return 해당 책에 속한 카테고리 리스트
	 */
	@Operation(
		summary = "책 ID로 카테고리 조회",
		description = "주어진 책 ID에 해당하는 카테고리 정보를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "책 ID로 카테고리 조회 성공")
	})
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
	@Operation(
		summary = "카테고리 ID로 카테고리 조회",
		description = "주어진 카테고리 ID에 해당하는 카테고리 정보를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "카테고리 ID로 카테고리 조회 성공"),
		@ApiResponse(responseCode = "404", description = "해당 카테고리를 찾을 수 없음")
	})
	@GetMapping("/{categoryId}")
	public ResponseEntity<GetCategoryResponse> getCategory(@PathVariable Long categoryId) {
		return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategory(categoryId));
	}

	/**
	 * 새로운 카테고리를 생성합니다.
	 *
	 * @param request 생성할 카테고리 정보 DTO
	 * @return 응답 상태 코드 (200 OK)
	 */
	@Operation(
		summary = "새 카테고리 생성",
		description = "새로운 카테고리를 생성합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "카테고리 생성 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다."),
		@ApiResponse(responseCode = "409", description = "이미 존재하는 카테고리입니다.")
	})
	@AuthorizeRole({"BOOK_ADMIN", "HEAD_ADMIN"})
	@PostMapping
	public ResponseEntity<Void> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
		categoryService.createCategory(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * 주어진 카테고리 ID에 해당하는 카테고리 정보를 업데이트합니다.
	 *
	 * @param categoryId 업데이트할 카테고리 ID
	 * @param request 업데이트할 카테고리 정보 DTO
	 * @return 응답 상태 코드 (200 OK)
	 */
	@Operation(
		summary = "카테고리 업데이트",
		description = "주어진 카테고리 ID에 해당하는 카테고리 정보를 업데이트합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "카테고리 업데이트 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다."),
		@ApiResponse(responseCode = "404", description = "해당 카테고리를 찾을 수 없음"),
		@ApiResponse(responseCode = "409", description = "이미 존재하는 카테고리입니다.")
	})
	@AuthorizeRole({"BOOK_ADMIN", "HEAD_ADMIN"})
	@PutMapping("/{categoryId}")
	public ResponseEntity<Void> updateCategory(@PathVariable Long categoryId,
		@Valid @RequestBody UpdateCategoryRequest request) {
		categoryService.updateCategory(categoryId, request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	/**
	 * 주어진 카테고리 ID에 해당하는 카테고리를 삭제합니다.
	 *
	 * @param categoryId 삭제할 카테고리 ID
	 * @return 응답 상태 코드 (200 OK)
	 */
	@Operation(
		summary = "카테고리 삭제",
		description = "주어진 카테고리 ID에 해당하는 카테고리를 삭제합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "카테고리 삭제 성공")
	})
	@AuthorizeRole({"BOOK_ADMIN", "HEAD_ADMIN"})
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
		categoryService.deleteCategory(categoryId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	/**
	 * @author 이기훈
	 * @param search 검색키워드
	 * @return 카테고리 검색결과
	 */
	@Operation(
		summary = "카테고리 검색",
		description = "검색키워드를 기반으로 카테고리를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "카테고리 검색 성공")
	})
	@AuthorizeRole({"COUPON_ADMIN", "HEAD_ADMIN"})
	@GetMapping("/search")
	public ResponseEntity<List<CategorySearchResult>> searchCategories(@RequestParam("key") String search) {
		List<CategorySearchResult> results = categoryService.searchCategories(search);
		return ResponseEntity.ok(results);
	}
}
