package com.nhnacademy.bookstoreback.book.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.JsonNode;
import com.nhnacademy.bookstoreback.book.domain.dto.request.CreateBookRequest;
import com.nhnacademy.bookstoreback.book.domain.dto.request.UpdateBookRequest;
import com.nhnacademy.bookstoreback.book.domain.dto.response.BookSearchResult;
import com.nhnacademy.bookstoreback.book.domain.dto.response.CreateBookResponse;
import com.nhnacademy.bookstoreback.book.domain.dto.response.GetBookDetailResponse;
import com.nhnacademy.bookstoreback.book.domain.dto.response.UpdateBookResponse;

/**
 * BookService 인터페이스
 * 도서 관련 서비스를 제공하는 인터페이스입니다.
 *
 * @version 1.0
 */
public interface BookService {

	/**
	 * 도서 리스트 조회 및 저장 (베스트셀러, 신간, 주목할만한 신간 등)
	 *
	 * @param apiUrl 도서 정보 API URL
	 */
	void fetchAndSaveBooks(String apiUrl);

	/**
	 * ISBN을 기준으로 도서 조회 및 저장
	 *
	 * @param apiURL 도서 정보 API URL
	 */
	void saveBookByIsbn(String apiURL);

	/**
	 * 도서 저장
	 *
	 * @param item 도서 정보
	 */
	void saveBook(JsonNode item) throws Exception;

	/**
	 * 모든 도서를 조회
	 *
	 * @return 도서 리스트를 포함하는 List 객체
	 */
	List<GetBookDetailResponse> findAllBooks();

	/**
	 * 모든 도서를 페이지네이션하여 조회
	 *
	 * @param pageable 페이지네이션 정보를 포함하는 객체
	 * @return 페이지네이션된 도서 리스트를 포함하는 Page 객체
	 */
	Page<GetBookDetailResponse> findAllBooks(Pageable pageable);

	/**
	 * 도서 ID를 기준으로 도서 조회
	 *
	 * @param bookId 도서 ID
	 * @return 도서 상세 정보
	 */
	GetBookDetailResponse getBook(Long bookId);

	/**
	 * ISBN을 기준으로 도서 조회
	 *
	 * @param isbn ISBN
	 * @return 도서 상세 정보
	 */
	GetBookDetailResponse findBookByIsbn(String isbn);

	/**
	 * 도서 생성
	 *
	 * @param request 생성할 도서 정보
	 * @return 생성된 도서 정보
	 */
	CreateBookResponse createBook(CreateBookRequest request);

	/**
	 * 도서 정보 수정
	 *
	 * @param bookId 수정할 도서 ID
	 * @param request 수정할 도서 정보
	 * @return 수정된 도서 정보
	 */
	UpdateBookResponse updateBookById(Long bookId, UpdateBookRequest request);

	/**
	 * 주어진 ID를 가진 도서를 삭제합니다.
	 * <p>
	 * 도서가 도서 주문과 연관되어 있지 않으면 삭제가 가능하며,
	 * 연관이 있을 경우 삭제가 불가능합니다.
	 * </p>
	 *
	 * @param bookId 삭제할 도서의 ID
	 */
	void deleteBook(Long bookId);



	/**
	 * 테스트용 서비스
	 */
	 List<BookSearchResult> searchBooks(String query);
}
