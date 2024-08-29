package com.nhnacademy.bookstoreback.book.repository;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author 김기욱
 * 도서 정보를 관리하는 Spring Data JPA 레포지토리입니다.
 */
public interface BookRepository extends JpaRepository<Book, Long>, CustomBookRepository {
    /**
     * ISBN 기반 도서 조회
     *
     * @param bookIsbn 도서 ISBN
     * @return 도서 정보 (단일 정보 반환하므로 Optional 사용)
     */
    Optional<Book> findByBookIsbn(String bookIsbn);

    /**
     * 주어진 제목을 가진 도서가 존재하는지 확인합니다.
     *
     * @param bookTitle 확인할 도서의 제목
     * @return 도서가 존재하면 true, 그렇지 않으면 false
     */
    boolean existsByBookTitle(String bookTitle);
}
