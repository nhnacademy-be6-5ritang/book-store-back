package com.nhnacademy.bookstoreback.book.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;

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

	/**
	 * 특정 카테고리명을 기준으로 도서를 필터링하여 페이지 단위로 반환합니다.
	 *
	 * @param pageable      페이지네이션 정보를 포함하는 객체로, 페이지 번호, 페이지 크기, 정렬 순서를 설정합니다.
	 * @param categoryName  도서가 포함된 카테고리의 이름으로 도서를 필터링합니다.
	 * @return 특정 카테고리명을 기준으로 필터링된 도서의 페이지
	 */
	Page<Book> findAllByBookCategories_Category_CategoryName(Pageable pageable, String categoryName);

	/**
	 * 도서 발행일을 기준으로 내림차순으로 정렬된 페이지를 반환합니다.
	 *
	 * @param pageable  페이지네이션 정보를 포함하는 객체로, 페이지 번호, 페이지 크기, 정렬 순서를 설정합니다.
	 * @return 도서 발행일을 기준으로 내림차순으로 정렬된 도서의 페이지
	 */
	Page<Book> findAllByOrderByBookPublishDateDesc(Pageable pageable);

	/**
	 * 사용자 위시리스트에서 가장 많이 좋아요를 받은 책을 반환합니다.
	 *
	 * @param pageable 페이징 및 정렬 정보를 포함한 Pageable 객체
	 * @return 좋아요 수에 따라 정렬된 책 목록
	 */
	@Query("SELECT b FROM Book b JOIN WishList wl ON b.bookId = wl.book.bookId " +
		"GROUP BY b.bookId ORDER BY COUNT(wl.book.bookId) DESC")
	Page<Book> findTopLikedBooks(Pageable pageable);

	/**
	 * 가장 많이 주문된 책을 반환합니다.
	 *
	 * @param pageable 페이징 및 정렬 정보를 포함한 Pageable 객체
	 * @return 주문 수에 따라 정렬된 책 목록
	 */
	@Query("SELECT b FROM Book b JOIN BookOrder bo ON b.bookId = bo.book.bookId " +
		"GROUP BY b.bookId ORDER BY COUNT(bo.book.bookId) DESC")
	Page<Book> findTopOrderedBooks(Pageable pageable);

	/**
	 * 주문되지 않은 책을 무작위로 반환합니다.
	 *
	 * @param pageable 페이징 및 정렬 정보를 포함한 Pageable 객체
	 * @return 무작위로 선택된, 주문되지 않은 책 목록
	 */
	@Query("SELECT b FROM Book b WHERE b.bookId NOT IN " +
		"(SELECT bo.book.bookId FROM BookOrder bo) ORDER BY RAND()")
	List<Book> findRandomOrderedBooks(Pageable pageable);

	/**
	 * 좋아요를 받지 않은 책을 무작위로 반환합니다.
	 *
	 * @param pageable 페이징 및 정렬 정보를 포함한 Pageable 객체
	 * @return 무작위로 선택된, 좋아요를 받지 않은 책 목록
	 */
	@Query("SELECT b FROM Book b WHERE b.bookId NOT IN " +
		"(SELECT wl.book.bookId FROM WishList wl) ORDER BY RAND()")
	List<Book> findRandomLikedBooks(Pageable pageable);
}
