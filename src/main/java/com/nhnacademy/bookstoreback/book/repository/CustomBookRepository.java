package com.nhnacademy.bookstoreback.book.repository;

import com.nhnacademy.bookstoreback.book.domain.dto.response.BookSearchResult;
import com.nhnacademy.bookstoreback.book.domain.dto.response.GetBookSimpleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author 이기훈
 * 쿼리dsl 적용 커스텀레포지토리
 */
public interface CustomBookRepository {

    /**
     * 지정된 카테고리 이름에 따라 책의 페이지 목록을 조회합니다.
     *
     * @param pageable     페이지 및 정렬 정보를 포함하는 객체입니다. 페이지 번호, 페이지 크기 및 정렬 기준을 포함합니다.
     * @param categoryName 책을 필터링할 카테고리의 이름입니다.
     * @return 지정된 카테고리 이름에 따라 필터링된 책의 간단 정보를 포함하는 페이지 객체입니다.
     */
    Page<GetBookSimpleResponse> findAllBooksByCategoryName(Pageable pageable, String categoryName);

    /**
     * 책검색 결과 반환하는 커스텀 메소드
     *
     * @param title 책제목
     * @return BookSearchResult
     * @author 이기훈
     */
    List<BookSearchResult> findByBookTitleContainingIgnoreCaseCustom(String title);
}
