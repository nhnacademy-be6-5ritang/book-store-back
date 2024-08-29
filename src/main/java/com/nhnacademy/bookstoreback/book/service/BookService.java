package com.nhnacademy.bookstoreback.book.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.JsonNode;
import com.nhnacademy.bookstoreback.book.domain.dto.request.CreateBookRequest;
import com.nhnacademy.bookstoreback.book.domain.dto.request.UpdateBookRequest;
import com.nhnacademy.bookstoreback.book.domain.dto.response.BookSearchResult;
import com.nhnacademy.bookstoreback.book.domain.dto.response.GetBookDetailResponse;
import com.nhnacademy.bookstoreback.book.domain.dto.response.GetBookResponse;
import com.nhnacademy.bookstoreback.book.domain.dto.response.GetBookTitleResponse;

/**
 * @author 김기욱, 이경헌
 * 도서 관련 서비스를 제공하는 인터페이스입니다.
 */
public interface BookService {

	/**
	 * 도서 리스트 조회 및 저장 (베스트셀러, 신간, 주목할만한 신간 등)
	 *
	 * @param apiUrl 도서 정보 API URL
	 */
	void fetchAndSaveBooks(String apiUrl);

	/**
	 * ISBN 을 기준으로 도서 조회 및 저장
	 *
	 * @param apiUrl 도서 정보 API URL
	 */
	void saveBookByIsbn(String apiUrl);

	/**
	 * 도서 저장
	 *
	 * @param item 도서 정보
	 */
	void saveBook(JsonNode item) throws Exception;

	/**
	 * 모든 도서를 페이지네이션하여 조회
	 *
	 * @param pageable 페이지네이션 정보를 포함하는 객체
	 * @return 페이지네이션된 도서 리스트를 포함하는 Page 객체
	 */
	Page<GetBookTitleResponse> getBooks(Pageable pageable);

	/**
	 * 도서 ID를 기준으로 조회
	 *
	 * @param bookId 도서 ID
	 * @return 도서 정보
	 */
	GetBookResponse getBook(Long bookId);

	/**
	 * ISBN 을 기준으로 도서 조회
	 *
	 * @param isbn ISBN
	 * @return 도서 상세 정보
	 */
	GetBookDetailResponse findBookByIsbn(String isbn);

	/**
	 * 도서 생성
	 *
	 * @param request 생성할 도서 정보
	 */
	void createBook(CreateBookRequest request);

	/**
	 * 도서 정보 수정
	 *
	 * @param bookId  수정할 도서 ID
	 * @param request 수정할 도서 정보
	 */
	void updateBookById(Long bookId, UpdateBookRequest request);

	/**
	 * 주어진 ID를 가진 도서를 삭제합니다.
	 *
	 * @param bookId 삭제할 도서의 ID
	 */
	void deleteBook(Long bookId);

	/**
	 * @author 이기훈
	 * 테스트용 서비스 (차후 리팩토링 예정)
	 */
	List<BookSearchResult> searchBooks(String query);

	/**
	 * 책의 수량을 업데이트합니다.
	 *
	 * @param bookId   책의 고유 ID
	 * @param quantity 업데이트할 수량
	 */
	void updateQuantity(Long bookId, int quantity);
}
