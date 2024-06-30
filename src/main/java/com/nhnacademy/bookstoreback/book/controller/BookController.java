package com.nhnacademy.bookstoreback.book.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.book.domain.dto.request.BookUpdateRequest;
import com.nhnacademy.bookstoreback.book.domain.dto.response.BookDetailResponse;
import com.nhnacademy.bookstoreback.book.domain.dto.response.BookListResponse;
import com.nhnacademy.bookstoreback.book.service.BookService;

import lombok.RequiredArgsConstructor;

/**
 * Book Controller
 *
 * @author 김기욱
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
	private final BookService bookService;

	/**
	 * 도서 포장 여부 업데이트
	 *
	 * @param bookId 도서 ID
	 */
	@GetMapping("/packaging/{book_id}")
	public void packaging(@PathVariable("book_id") Long bookId) {
		bookService.updateBookById(bookId);
	}

	/**
	 * 도서 리스트 조회 및 저장 (베스트셀러, 신간, 주목할만한 신간 등)
	 *
	 * @return 도서저장결과
	 */
	@PostMapping("/fetch/book-lists")
	public String fetchAndSaveBooks() {
		try {
			String apiUrl =
				"http://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey=ttb2897robo0933001&QueryType=BlogBest&MaxResults=10&start=1&SearchTarget=Book&output=js&Version=20131101";
			bookService.fetchAndSaveBooks(apiUrl);
			return "도서들 목록이 성공적으로 저장되었습니다.";
		} catch (Exception e) {
			return "도서들 목록 저장 중 에러 발생 : " + e.getMessage();
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
	 * 도서 리스트 조회
	 *
	 * @return 도서 리스트
	 */
	@GetMapping
	public List<BookListResponse> findAllBooks() {
		return bookService.findAllBooks();
	}

	/**
	 * ISBN을 통한 도서 상세페이지 조회
	 *
	 * @param isbn 도서 ISBN
	 * @return 도서 상세페이지
	 */
	@GetMapping("/details/{isbn}")
	public BookDetailResponse findBookByIsbn(@PathVariable String isbn) {
		return bookService.findBookByIsbn(isbn);
	}

	/**
	 * ISBN을 통한 도서정보 수정
	 *
	 * @param isbn 도서 ISBN
	 * @param request 수정할 도서 정보
	 * @return 수정된 도서 상세페이지
	 */
	@PatchMapping("/update/{isbn}")
	public BookDetailResponse updateBook(@PathVariable String isbn, @RequestBody BookUpdateRequest request) {
		return bookService.updateBook(isbn, request);
	}

	@GetMapping("/{bookId}")
	public ResponseEntity<BookDetailResponse> getBook(@PathVariable Long bookId) {
		return ResponseEntity.status(HttpStatus.OK).body(bookService.findBookById(bookId));
	}

	// @PostMapping
	// public ResponseEntity<Create>
}
