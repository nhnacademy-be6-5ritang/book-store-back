package com.nhnacademy.bookstoreback.book.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

	/**
	 * 주어진 제목을 가진 도서가 존재하는지 확인합니다.
	 *
	 * @param bookTitle 확인할 도서의 제목
	 * @return 도서가 존재하면 true, 그렇지 않으면 false
	 */
	boolean existsByBookTitle(String bookTitle);



	/**
	 * 쿠폰 테스트용
	 *
	 *
	 */
	@Query(value = "SELECT * FROM books WHERE lower(book_title) LIKE lower(concat('%', :title, '%'))", nativeQuery = true)
	List<Book> findByBookTitleContainingIgnoreCaseCustom(@Param("title") String title);



}

