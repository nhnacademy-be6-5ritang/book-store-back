package com.nhnacademy.bookstoreback.book.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.nhnacademy.bookstoreback.book.domain.dto.request.CreateBookRequest;
import com.nhnacademy.bookstoreback.book.domain.dto.request.UpdateBookRequest;
import com.nhnacademy.bookstoreback.book.domain.dto.response.BookSearchResult;
import com.nhnacademy.bookstoreback.book.domain.dto.response.GetBookDetailResponse;
import com.nhnacademy.bookstoreback.book.domain.dto.response.GetBookSimpleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
    Page<GetBookDetailResponse> findAllBooks(Pageable pageable);

    /**
     * 도서 ID를 기준으로 도서 조회
     *
     * @param bookId 도서 ID
     * @return 도서 상세 정보
     */
    GetBookDetailResponse getBook(Long bookId);

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
     * 주문된 책들의 목록을 조회합니다.
     *
     * @return 주문된 책들의 세부 정보를 담고 있는 {@link GetBookDetailResponse} 객체의 리스트를 반환합니다.
     */
    List<GetBookDetailResponse> getOrderedBooks();

    /**
     * 좋아요가 눌린 책들의 목록을 조회합니다.
     *
     * @return 좋아요가 눌린 책들의 세부 정보를 담고 있는 {@link GetBookDetailResponse} 객체의 리스트를 반환합니다.
     */
    List<GetBookDetailResponse> getLikesBooks();

    /**
     * 신간 도서 리스트를 조회
     *
     * @return 신간 도서 리스트를 포함하는 List 객체
     */
    List<GetBookDetailResponse> getNewestBooks();

    /**
     * 책의 수량을 업데이트합니다.
     *
     * @param bookId   책의 고유 ID
     * @param quantity 업데이트할 수량
     */
    void updateQuantity(Long bookId, int quantity);

    /**
     * 지정된 카테고리에 해당하는 책 목록을 조회합니다.
     *
     * @param pageable 페이지 정보와 정렬 기준을 포함하는 객체입니다. 페이지 번호, 페이지 크기 및 정렬 정보를 설정합니다.
     * @param category 조회할 책의 카테고리 이름입니다. 이 카테고리에 속하는 책들만 조회됩니다.
     * @return 지정된 카테고리에 속하는 책들의 {@link GetBookSimpleResponse} 객체 목록입니다.
     */
    Page<GetBookSimpleResponse> getBooksByCategory(Pageable pageable, String categoryName);
}
