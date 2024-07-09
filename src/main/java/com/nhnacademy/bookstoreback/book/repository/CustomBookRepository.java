package com.nhnacademy.bookstoreback.book.repository;

import java.util.List;

import com.nhnacademy.bookstoreback.book.domain.dto.response.BookSearchResult;



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
}