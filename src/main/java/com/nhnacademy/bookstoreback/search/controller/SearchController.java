package com.nhnacademy.bookstoreback.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Logger;

import com.nhnacademy.bookstoreback.search.dto.reponse.BookSearchResponse;
import com.nhnacademy.bookstoreback.search.service.SearchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 검색 관련 HTTP 요청을 처리하는 컨트롤러입니다.
 * 이 컨트롤러는 책, 저자, 출판사 및 태그에 대한 검색 기능을 제공합니다.
 *
 * @version 1.0
 */
@Tag(name = "Search", description = "검색 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

	private static final Logger logger = Logger.getLogger(SearchController.class.getName());

	@Autowired
	private final SearchService searchService;

	/**
	 * 책을 검색합니다.
	 *
	 * @param query    검색어
	 * @param pageable 페이지 정보
	 * @return 검색 결과 페이지
	 * @throws IOException I/O 예외가 발생할 수 있습니다.
	 */
	@Operation(
		summary = "책 검색",
		description = "주어진 검색어로 책을 검색합니다"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "책 검색이 성공적으로 완료되었습니다."),
		@ApiResponse(responseCode = "400", description = "잘못된 검색어입니다."),
		@ApiResponse(responseCode = "500", description = "서버 오류로 검색을 완료할 수 없습니다.")
	})
	@GetMapping("/books")
	public ResponseEntity<Page<BookSearchResponse>> searchBooks(
		@RequestParam String query,
		@PageableDefault(page = 1, size = 20) Pageable pageable) throws IOException {
		logger.info("컨트롤러 Received request to search books with query: " + query);
		return ResponseEntity.status(HttpStatus.OK).body(searchService.searchBooks(query, pageable));
	}

	/**
	 * 저자를 검색합니다.
	 *
	 * @param query    검색어
	 * @param pageable 페이지 정보
	 * @return 검색 결과 페이지
	 * @throws IOException I/O 예외가 발생할 수 있습니다.
	 */
	@Operation(
		summary = "저자 검색",
		description = "주어진 검색어로 저자를 검색합니다"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "저자 검색이 성공적으로 완료되었습니다."),
		@ApiResponse(responseCode = "400", description = "잘못된 검색어입니다."),
		@ApiResponse(responseCode = "500", description = "서버 오류로 검색을 완료할 수 없습니다.")
	})
	@GetMapping("/authors")
	public ResponseEntity<Page<BookSearchResponse>> searchAuthors(
		@RequestParam String query,
		@PageableDefault(page = 1, size = 20) Pageable pageable) throws IOException {
		logger.info("컨트롤러 Received request to search authors with query: " + query);
		return ResponseEntity.status(HttpStatus.OK).body(searchService.searchAuthors(query, pageable));
	}

	/**
	 * 출판사를 검색합니다.
	 *
	 * @param query    검색어
	 * @param pageable 페이지 정보
	 * @return 검색 결과 페이지
	 * @throws IOException I/O 예외가 발생할 수 있습니다.
	 */
	@Operation(
		summary = "출판사 검색",
		description = "주어진 검색어로 출판사를 검색합니다"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "출판사 검색이 성공적으로 완료되었습니다."),
		@ApiResponse(responseCode = "400", description = "잘못된 검색어입니다."),
		@ApiResponse(responseCode = "500", description = "서버 오류로 검색을 완료할 수 없습니다.")
	})
	@GetMapping("/publisher")
	public ResponseEntity<Page<BookSearchResponse>> searchPublishers(
		@RequestParam String query,
		@PageableDefault(page = 1, size = 20) Pageable pageable) throws IOException {
		logger.info("컨트롤러 Received request to search publishers with query: " + query);
		return ResponseEntity.status(HttpStatus.OK).body(searchService.searchPublishers(query, pageable));
	}

	/**
	 * 태그로 책을 검색합니다.
	 *
	 * @param query    검색어
	 * @param pageable 페이지 정보
	 * @return 검색 결과 페이지
	 * @throws IOException I/O 예외가 발생할 수 있습니다.
	 */
	@Operation(
		summary = "태그로 책 검색",
		description = "주어진 태그로 책을 검색합니다"
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "태그로 책 검색이 성공적으로 완료되었습니다."),
		@ApiResponse(responseCode = "400", description = "잘못된 검색어입니다."),
		@ApiResponse(responseCode = "500", description = "서버 오류로 검색을 완료할 수 없습니다.")
	})
	@GetMapping("/tag")
	public ResponseEntity<Page<BookSearchResponse>> searchBooksByTag(
		@RequestParam String query,
		@PageableDefault(page = 1, size = 20) Pageable pageable) throws IOException {
		logger.info("컨트롤러 Received request to search books by tag with query: " + query);
		return ResponseEntity.status(HttpStatus.OK).body(searchService.searchBooksByTag(query, pageable));
	}
}
