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

import com.nhnacademy.bookstoreback.auth.annotation.AuthorizeRole;
import com.nhnacademy.bookstoreback.book.domain.dto.request.CreateBookRequest;
import com.nhnacademy.bookstoreback.book.domain.dto.request.UpdateBookRequest;
import com.nhnacademy.bookstoreback.book.domain.dto.response.BookSearchResult;
import com.nhnacademy.bookstoreback.book.domain.dto.response.GetBookDetailResponse;
import com.nhnacademy.bookstoreback.book.service.impl.BookServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Book Controller
 *
 * @author 김기욱
 * @version 1.0
 */
@Tag(name = "Book", description = "도서 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {
	private final BookServiceImpl bookService;

	/**
	 * 도서 리스트 조회 및 저장 (국내도서&외국도서 베스트셀러 count 만큼 저장)
	 *
	 * @return 도서저장결과
	 */
	@Operation(
		summary = "도서 베스트 셀러 다권 랜덤 저장",
		description = "알라딘 API 를 활용하여 최대 100권의 국내, 해외도서를 추가합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "베스트 셀러 도서 리스트가 생성되었습니다."),
		@ApiResponse(responseCode = "500", description = "베스트 셀러 도서 리스트 저장에 실패하였습니다.")
	})
	// @AuthorizeRole({"BOOK_ADMIN", "HEAD_ADMIN"})
	@PostMapping("/fetch/book-lists")
	public ResponseEntity<String> fetchAndSaveBooks(@RequestParam Long count) {
		try {
			String apiUrl =
				"http://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey=ttb2897robo0933001&QueryType=Bestseller&MaxResults="
					+ count + "&start=1&SearchTarget=Book&output=js&Version=20131101";
			bookService.fetchAndSaveBooks(apiUrl);
			apiUrl =
				"http://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey=ttb2897robo0933001&QueryType=Bestseller&MaxResults="
					+ count + "&start=1&SearchTarget=Foreign&CategoryId=90838&output=js&Version=20131101";
			bookService.fetchAndSaveBooks(apiUrl);
			return ResponseEntity.status(HttpStatus.OK).body("도서들 목록이 성공적으로 저장되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	/**
	 * ISBN 을 통한 도서 한권 조회 및 저장
	 *
	 * @return 도서저장결과
	 */
	@Operation(
		summary = "ISBN 도서 추가 ",
		description = "알라딘 API 를 이용하여 ISBN 에 해당하는 도서를 추가합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "ISBN 에 해당하는 도서가 성공적으로 생성되었습니다."),
		@ApiResponse(responseCode = "500", description = "요청한 도서 저장에 실패하였습니다.")
	})
	@AuthorizeRole({"BOOK_ADMIN", "HEAD_ADMIN"})
	@PostMapping("/fetch")
	public ResponseEntity<Void> saveBookByIsbn(@RequestParam String isbn) {
		try {
			String apiUrl =
				"http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?ttbkey=ttb2897robo0933001&itemIdType=ISBN&ItemId="
					+ isbn + "&output=js&Version=20131101";
			bookService.saveBookByIsbn(apiUrl);
			log.info("ISBN: {} 기반으로 한 도서정보가 성공적으로 저장되었습니다.", isbn);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			log.error("ISBN: {} 기반으로 도서 저장 중 오류 발생 : ", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * 최신 도서 목록을 조회합니다.
	 *
	 * @return 최신 도서 목록이 포함된 {@link ResponseEntity} 객체
	 *         - HTTP 상태 코드 200 (OK)
	 *         - 본문에는 {@link GetBookDetailResponse} 객체의 리스트가 포함됩니다.
	 */
	@Operation(
		summary = "최신 도서 목록 조회",
		description = "최신 도서 목록을 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "최신 도서 목록이 성공적으로 조회되었습니다."),
		@ApiResponse(responseCode = "500", description = "서버 오류로 인해 도서 목록을 조회할 수 없습니다.")
	})
	@GetMapping
	public ResponseEntity<List<GetBookDetailResponse>> getNewestBooks() {
		return ResponseEntity.status(HttpStatus.OK).body(bookService.getNewestBooks());
	}

	/**
	 * 베스트셀러 도서 목록을 조회합니다.
	 *
	 * @return 베스트셀러 도서 목록이 포함된 {@link ResponseEntity} 객체
	 *         - HTTP 상태 코드 200 (OK)
	 *         - 본문에는 {@link GetBookDetailResponse} 객체의 리스트가 포함됩니다.
	 */
	@Operation(
		summary = "베스트셀러 도서 목록 조회",
		description = "베스트셀러 도서 목록을 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "베스트셀러 도서 목록이 성공적으로 조회되었습니다."),
		@ApiResponse(responseCode = "500", description = "서버 오류로 인해 도서 목록을 조회할 수 없습니다.")
	})
	@GetMapping("/ordered")
	public ResponseEntity<List<GetBookDetailResponse>> getOrderedBooks() {
		return ResponseEntity.status(HttpStatus.OK).body(bookService.getOrderedBooks());
	}

	/**
	 * 좋아요가 많은 도서 목록을 조회합니다.
	 *
	 * @return 좋아요가 많은 도서 목록이 포함된 {@link ResponseEntity} 객체
	 *         - HTTP 상태 코드 200 (OK)
	 *         - 본문에는 {@link GetBookDetailResponse} 객체의 리스트가 포함됩니다.
	 */
	@Operation(
		summary = "좋아요가 많은 도서 목록 조회",
		description = "좋아요가 많은 도서 목록을 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "좋아요가 많은 도서 목록이 성공적으로 조회되었습니다."),
		@ApiResponse(responseCode = "500", description = "서버 오류로 인해 도서 목록을 조회할 수 없습니다.")
	})
	@GetMapping("/likes")
	public ResponseEntity<List<GetBookDetailResponse>> getLikesBooks() {
		return ResponseEntity.status(HttpStatus.OK).body(bookService.getLikesBooks());
	}

	/**
	 * 모든 도서의 리스트를 페이지 형태로 조회
	 *
	 * @param pageable 페이지네이션 정보를 포함하는 객체
	 * @return 페이지네이션 된 도서 리스트를 포함하는 ResponseEntity 객체
	 */
	@Operation(
		summary = "모든 도서 리스트 조회 (페이지네이션)",
		description = "모든 도서를 페이지 형태로 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "도서 리스트가 성공적으로 조회되었습니다."),
		@ApiResponse(responseCode = "500", description = "서버 오류로 인해 도서 리스트를 조회할 수 없습니다.")
	})
	@GetMapping("/page")
	public ResponseEntity<Page<GetBookDetailResponse>> getNewestBooks(
		@PageableDefault(page = 1, size = 10) Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(bookService.findAllBooks(pageable));
	}

	/**
	 * ISBN 을 통한 도서 상세페이지 조회
	 *
	 * @param isbn 도서 ISBN
	 * @return 도서 상세페이지
	 */
	@Operation(
		summary = "ISBN 을 통한 도서 조회",
		description = "주어진 ISBN 을 통해 도서의 상세 정보를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "도서 상세 정보가 성공적으로 조회되었습니다."),
		@ApiResponse(responseCode = "404", description = "ISBN 으로 조회된 도서를 찾을 수 없습니다."),
		@ApiResponse(responseCode = "500", description = "서버 오류로 인해 도서 상세 정보를 조회할 수 없습니다.")
	})
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
	@Operation(
		summary = "도서 ID를 통한 도서 조회",
		description = "주어진 도서 ID를 통해 도서의 상세 정보를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "도서 상세 정보가 성공적으로 조회되었습니다."),
		@ApiResponse(responseCode = "404", description = "주어진 ID의 도서를 찾을 수 없습니다."),
		@ApiResponse(responseCode = "500", description = "서버 오류로 인해 도서 상세 정보를 조회할 수 없습니다.")
	})
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
	@Operation(
		summary = "새로운 도서 생성",
		description = "새로운 도서를 생성합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "새로운 도서가 성공적으로 생성되었습니다."),
		@ApiResponse(responseCode = "400", description = "잘못된 도서 데이터입니다."),
		@ApiResponse(responseCode = "409", description = "해당 도서가 이미 존재합니다."),
		@ApiResponse(responseCode = "500", description = "서버 오류로 인해 도서를 생성할 수 없습니다.")
	})
	@AuthorizeRole({"BOOK_ADMIN", "HEAD_ADMIN"})
	@PostMapping
	public ResponseEntity<Void> createBook(
		@Valid @RequestBody CreateBookRequest request) {
		bookService.createBook(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * 특정 도서를 수정합니다.
	 *
	 * @param bookId 수정할 도서의 ID
	 * @param request 수정할 도서의 정보
	 * @return 수정된 도서의 응답 정보
	 */
	@Operation(
		summary = "도서 수정",
		description = "주어진 도서 ID를 통해 도서를 수정합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "도서가 성공적으로 수정되었습니다."),
		@ApiResponse(responseCode = "400", description = "잘못된 도서 데이터입니다."),
		@ApiResponse(responseCode = "404", description = "수정할 도서를 찾을 수 없습니다."),
		@ApiResponse(responseCode = "500", description = "서버 오류로 인해 도서를 수정할 수 없습니다.")
	})
	@AuthorizeRole({"BOOK_ADMIN", "HEAD_ADMIN"})
	@PostMapping("/{bookId}")
	public ResponseEntity<Void> updateBookByBookId(@PathVariable Long bookId,
		@Valid @RequestBody UpdateBookRequest request) {
		bookService.updateBookById(bookId, request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@Operation(
		summary = "도서 삭제",
		description = "주어진 도서 ID를 통해 도서를 삭제합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "도서가 성공적으로 삭제되었습니다."),
		@ApiResponse(responseCode = "500", description = "서버 오류로 인해 도서를 삭제할 수 없습니다.")
	})
	@AuthorizeRole({"BOOK_ADMIN", "HEAD_ADMIN"})
	@DeleteMapping("/{bookId}")
	public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
		bookService.deleteBook(bookId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@Operation(
		summary = "도서 수량 업데이트",
		description = "주어진 도서 ID를 통해 도서의 수량을 업데이트합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "도서 수량이 성공적으로 업데이트되었습니다."),
		@ApiResponse(responseCode = "404", description = "수량을 업데이트할 도서를 찾을 수 없습니다."),
		@ApiResponse(responseCode = "500", description = "서버 오류로 인해 도서 수량을 업데이트할 수 없습니다.")
	})
	@PutMapping("/{bookId}/{quantity}")
	public ResponseEntity<Void> updateQuantity(@PathVariable Long bookId, @PathVariable int quantity) {
		bookService.updateQuantity(bookId, quantity);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	/**
	 * @author 이기훈
	 * @param search 검색키워드
	 * @return 도서 검색결과
	 */
	@Operation(
		summary = "도서 검색",
		description = "검색키워드를 기반으로 도서를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "도서 검색 성공")
	})
	@AuthorizeRole({"COUPON_ADMIN", "HEAD_ADMIN"})
	@GetMapping("/search")
	public ResponseEntity<List<BookSearchResult>> searchBooks(@RequestParam("key") String search) {
		List<BookSearchResult> results = bookService.searchBooks(search);
		return ResponseEntity.ok(results);
	}

	/**
	 * 특정 카테고리 이름에 해당하는 모든 책을 페이징하여 조회합니다.
	 *
	 * @param pageable 페이징 정보 (페이지 번호 및 페이지 크기)
	 * @param categoryName 검색할 카테고리 이름
	 * @return 카테고리 이름에 해당하는 책의 페이징된 목록
	 */
	@Operation(
		summary = "카테고리 이름으로 책 검색",
		description = "특정 카테고리 이름에 해당하는 모든 책을 페이징하여 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "카테고리 이름으로 책 검색 성공")
	})
	@GetMapping("/page/category")
	public ResponseEntity<Page<GetBookDetailResponse>> findAllBooksByCategoryName(
		@PageableDefault(page = 1, size = 12) Pageable pageable, @RequestParam String categoryName) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(bookService.findAllBooksByCategoryName(pageable, categoryName));
	}
}
