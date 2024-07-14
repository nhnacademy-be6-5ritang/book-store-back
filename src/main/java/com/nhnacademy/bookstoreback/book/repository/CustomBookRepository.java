package com.nhnacademy.bookstoreback.book.repository;

import java.util.List;

import com.nhnacademy.bookstoreback.book.domain.dto.response.BookSearchResult;
import com.nhnacademy.bookstoreback.book.domain.dto.response.GetBookTitleResponse;

/**
 * @author 이기훈
 * 쿼리dsl 적용 커스텀레포지토리
 *
 */
public interface CustomBookRepository {

	/**
	 * @author 이기훈
	 * @param  title 책제목
	 * @return 책검색 결과 반환하는 커스텀 메소드
	 *
	 */
	List<BookSearchResult> findByBookTitleContainingIgnoreCaseCustom(String title);

	/**
	 * @author 이경헌
	 * @param  orderStatusName 주문 상태
	 * @param  userId 사용자Id
	 * @return 리뷰를 작성하기 위한 "배송 완료" 상태를 가진 주문중에 도서 리시트 결과를 반환하는 커스텀 메소드
	 *
	 */
	List<GetBookTitleResponse> getBooksByOrderStatusCompletionAndUserId(String orderStatusName, Long userId);
}