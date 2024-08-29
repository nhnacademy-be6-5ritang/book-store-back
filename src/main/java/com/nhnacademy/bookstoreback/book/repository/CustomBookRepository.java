package com.nhnacademy.bookstoreback.book.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstoreback.book.domain.dto.response.BookSearchResult;
import com.nhnacademy.bookstoreback.book.domain.dto.response.GetBookResponse;
import com.nhnacademy.bookstoreback.book.domain.dto.response.GetBookTitleResponse;

/**
 * @author 이기훈
 * 쿼리dsl 적용 커스텀레포지토리
 */
public interface CustomBookRepository {

	/**
	 * 특정 도서를 조회합니다.
	 *
	 * @param bookId 조회할 도서의 ID
	 * @return 도서의 상세 정보
	 */
	Optional<GetBookResponse> getBook(Long bookId);

	/**
	 * 모든 책의 페이지 목록을 조회합니다.
	 *
	 * @param pageable 페이지 및 정렬 정보를 포함하는 객체입니다. 페이지 번호, 페이지 크기 및 정렬 기준을 포함합니다.
	 * @return 모든 책의 간단 정보를 포함하는 페이지 객체입니다.
	 */
	Page<GetBookTitleResponse> getBooks(Pageable pageable);

	/**
	 * 책검색 결과 반환하는 커스텀 메소드
	 *
	 * @param title 책제목
	 * @return BookSearchResult
	 * @author 이기훈
	 */
	List<BookSearchResult> findByBookTitleContainingIgnoreCaseCustom(String title);
}
