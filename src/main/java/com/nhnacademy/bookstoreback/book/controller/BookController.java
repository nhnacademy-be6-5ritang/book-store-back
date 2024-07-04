package com.nhnacademy.bookstoreback.book.controller;

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

import com.nhnacademy.bookstoreback.book.domain.dto.request.CreateBookRequest;
import com.nhnacademy.bookstoreback.book.domain.dto.request.UpdateBookRequest;
import com.nhnacademy.bookstoreback.book.domain.dto.response.CreateBookResponse;
import com.nhnacademy.bookstoreback.book.domain.dto.response.GetBookDetailResponse;
import com.nhnacademy.bookstoreback.book.domain.dto.response.UpdateBookResponse;
import com.nhnacademy.bookstoreback.book.service.impl.BookServiceImpl;

import lombok.RequiredArgsConstructor;

/**
 * Book Controller
 *
 * @author 김기욱
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {
	private final BookServiceImpl bookService;

	/**
	 * 도서 리스트 조회 및 저장 (베스트셀러, 신간, 주목할만한 신간 등)
	 *
	 * @return 도서저장결과
	 */
	@PostMapping("/fetch/book-lists")
	public ResponseEntity<String> fetchAndSaveBooks(@RequestParam Long count) {
		try {
			String apiUrl =
				"http://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey=ttb2897robo0933001&QueryType=BlogBest&MaxResults="
					+ count + "&start=1&SearchTarget=Book&output=js&Version=20131101";
			bookService.fetchAndSaveBooks(apiUrl);
			return ResponseEntity.status(HttpStatus.OK).body("도서들 목록이 성공적으로 저장되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	/**
	 * ISBN을 통한 도서정보 조회 및 저장
	 *
	 * @return 도서저장결과
	 */
	@PostMapping("/fetch/{isbn}")
	public String saveBookByIsbn(@PathVariable String isbn) {
		try {
			String apiUrl =
				"http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?ttbkey=ttb2897robo0933001&itemIdType=ISBN&ItemId="
					+ isbn + "&output=js&Version=20131101";
			bookService.saveBookByIsbn(apiUrl);
			return "ISBN을 기반으로 한 도서정보가 성공적으로 저장되었습니다.";
		} catch (Exception e) {
			return "ISBN을 기반으로 도서 저장 중 오류 발생 : " + e.getMessage();
		}
	}

	/**
	 * 모든 도서의 리스트를  조회
	 *
	 * @return 도서 리스트를 포함하는 ResponseEntity 객체
	 */
	@GetMapping
	public ResponseEntity<List<GetBookDetailResponse>> findAllBooks() {
		return ResponseEntity.status(HttpStatus.OK).body(bookService.findAllBooks());
	}

	/**
	 * 모든 도서의 리스트를 페이지 형태로 조회
	 *
	 * @param pageable 페이지네이션 정보를 포함하는 객체
	 * @return 페이지네이션 된 도서 리스트를 포함하는 ResponseEntity 객체
	 */
	@GetMapping("/page")
	public ResponseEntity<Page<GetBookDetailResponse>> findAllBooks(
		@PageableDefault(page = 1, size = 10) Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(bookService.findAllBooks(pageable));
	}

	/**
	 * ISBN을 통한 도서 상세페이지 조회
	 *
	 * @param isbn 도서 ISBN
	 * @return 도서 상세페이지
	 */
	@GetMapping("/details/{isbn}")
	public ResponseEntity<GetBookDetailResponse> findBookByIsbn(@PathVariable String isbn) {
		return ResponseEntity.status(HttpStatus.OK).body(bookService.findBookByIsbn(isbn));
	}

	/**
	 * 특정 도서를 조회합니다.
	 *
	 * @param bookId 조회할 도서의 ID
	 * @return 도서의 상세 정보
	 */
	@GetMapping("/{bookId}")
	public ResponseEntity<GetBookDetailResponse> getBook(@PathVariable Long bookId) {
		return ResponseEntity.status(HttpStatus.OK).body(bookService.getBook(bookId));
	}

	/**
	 * 새로운 도서를 생성합니다.
	 *
	 * @param request 생성할 도서의 정보
	 * @return 생성된 도서의 응답 정보
	 */
	@PostMapping
	public ResponseEntity<CreateBookResponse> createBook(
		@RequestBody CreateBookRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(request));
	}

	/**
	 * 특정 도서를 수정합니다.
	 *
	 * @param bookId 수정할 도서의 ID
	 * @param request 수정할 도서의 정보
	 * @return 수정된 도서의 응답 정보
	 */
	@PutMapping("/{bookId}")
	public ResponseEntity<UpdateBookResponse> updateBookByBookId(@PathVariable Long bookId,
		@RequestBody UpdateBookRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(bookService.updateBookById(bookId, request));
	}

	@DeleteMapping("/{bookId}")
	public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
		bookService.deleteBook(bookId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
