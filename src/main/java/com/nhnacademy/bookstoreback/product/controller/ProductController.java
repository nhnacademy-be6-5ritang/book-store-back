package com.nhnacademy.bookstoreback.product.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.book.domain.dto.response.GetBookDetailResponse;
import com.nhnacademy.bookstoreback.product.dto.response.GetProductResponse;
import com.nhnacademy.bookstoreback.product.dto.response.GetProductSimpleResponse;
import com.nhnacademy.bookstoreback.product.service.impl.ProductServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @author 이경헌
 * 상품 관련된 API를 제공하는 컨트롤러 클래스입니다.
 */
@Tag(name = "Book", description = "도서 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
	private final ProductServiceImpl productService;

	/**
	 * 베스트셀러 상품 목록을 조회합니다.
	 *
	 * @return 베스트셀러 상품 목록이 포함된 {@link ResponseEntity} 객체
	 * - HTTP 상태 코드 200 (OK)
	 * - 본문에는 {@link GetBookDetailResponse} 객체의 리스트가 포함됩니다.
	 */
	@Operation(
		summary = "베스트셀러 상품 목록 조회",
		description = "베스트셀러 상품 목록을 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "베스트셀러 상품 목록이 성공적으로 조회되었습니다."),
		@ApiResponse(responseCode = "500", description = "서버 오류로 인해 상품 목록을 조회할 수 없습니다.")
	})
	@GetMapping("/best-seller")
	ResponseEntity<List<GetProductSimpleResponse>> getBestSellerBooks() {
		return ResponseEntity.status(HttpStatus.OK).body(productService.getBestSellerBooks());
	}

	/**
	 * 최신 상품 목록을 조회합니다.
	 *
	 * @return 최신 상품 목록이 포함된 {@link ResponseEntity} 객체
	 * - HTTP 상태 코드 200 (OK)
	 * - 본문에는 {@link GetBookDetailResponse} 객체의 리스트가 포함됩니다.
	 */
	@Operation(
		summary = "최신 상품 목록 조회",
		description = "최신 상품 목록을 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "최신 상품 목록이 성공적으로 조회되었습니다."),
		@ApiResponse(responseCode = "500", description = "서버 오류로 인해 상품 목록을 조회할 수 없습니다.")
	})
	@GetMapping("/newest")
	ResponseEntity<List<GetProductSimpleResponse>> getNewestBooks() {
		return ResponseEntity.status(HttpStatus.OK).body(productService.getNewestBooks());
	}

	/**
	 * 좋아요가 많은 상품 목록을 조회합니다.
	 *
	 * @return 좋아요가 많은 상품 목록이 포함된 {@link ResponseEntity} 객체
	 * - HTTP 상태 코드 200 (OK)
	 * - 본문에는 {@link GetBookDetailResponse} 객체의 리스트가 포함됩니다.
	 */
	@Operation(
		summary = "좋아요가 많은 상품 목록 조회",
		description = "좋아요가 많은 상품 목록을 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "좋아요가 많은 상품 목록이 성공적으로 조회되었습니다."),
		@ApiResponse(responseCode = "500", description = "서버 오류로 인해 상품 목록을 조회할 수 없습니다.")
	})
	@GetMapping("/likes")
	ResponseEntity<List<GetProductSimpleResponse>> getLikesBooks() {
		return ResponseEntity.status(HttpStatus.OK).body(productService.getLikesBooks());
	}

	/**
	 * 특정 카테고리 이름에 해당하는 모든 상품을 페이징하여 조회합니다.
	 *
	 * @param pageable     페이징 정보 (페이지 번호 및 페이지 크기)
	 * @param categoryName 검색할 카테고리 이름
	 * @return 카테고리 이름에 해당하는 상품의 페이징된 목록
	 */
	@Operation(
		summary = "카테고리 이름으로 상품 검색",
		description = "특정 카테고리 이름에 해당하는 모든 상품을 페이징하여 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "카테고리 이름으로 상품 검색 성공")
	})
	@GetMapping("/category")
	ResponseEntity<Page<GetProductSimpleResponse>> getLikesBooks(
		@PageableDefault(page = 1, size = 12) Pageable pageable, @RequestParam String categoryName) {
		return ResponseEntity.status(HttpStatus.OK).body(productService.getBooksByCategory(pageable, categoryName));
	}

	/**
	 * 특정 상품을 조회합니다.
	 *
	 * @param bookId 조회할 도서의 ID
	 * @return 상품의 상세 정보
	 */
	@Operation(
		summary = "도서 ID를 통한 상품 조회",
		description = "주어진 도서 ID를 통해 상품의 상세 정보를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "상품 상세 정보가 성공적으로 조회되었습니다."),
		@ApiResponse(responseCode = "404", description = "주어진 ID의 상품을 찾을 수 없습니다."),
		@ApiResponse(responseCode = "500", description = "서버 오류로 인해 상품 상세 정보를 조회할 수 없습니다.")
	})
	@GetMapping("/{bookId}")
	public ResponseEntity<GetProductResponse> getProduct(@PathVariable Long bookId) {
		return ResponseEntity.status(HttpStatus.OK).body(productService.getProduct(bookId));
	}

}
