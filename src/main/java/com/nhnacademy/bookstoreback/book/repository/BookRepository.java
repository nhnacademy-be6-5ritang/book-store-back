package com.nhnacademy.bookstoreback.book.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;

/**
 * 도서 Repository
 *
 * @author 김기욱
 * @version 1.0
 */

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	/**
	 * ISBN 기반 도서 조회
	 *
	 * @param bookIsbn 도서 ISBN
	 * @return 도서 정보 (단일 정보 반환하므로 Optional 사용)
	 */
	Optional<Book> findByBookIsbn(String bookIsbn);
	
	boolean existsByBookTitle(String bookTitle);
}
