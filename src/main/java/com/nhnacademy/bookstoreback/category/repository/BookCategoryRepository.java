package com.nhnacademy.bookstoreback.category.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.category.domain.entity.BookCategory;

/**
 * BookCategory 엔티티를 관리하는 Spring Data JPA 리포지토리입니다.
 */
public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {
	/**
	 * 주어진 책 ID에 해당하는 모든 BookCategory 엔티티를 삭제합니다.
	 *
	 * @param bookId 삭제할 책의 ID
	 */
	void deleteAllByBookBookId(Long bookId);

	/**
	 * 주어진 카테고리 ID에 해당하는 모든 BookCategory 엔티티를 삭제합니다.
	 *
	 * @param categoryId 삭제할 카테고리의 ID
	 */
	void deleteALlByCategoryCategoryId(Long categoryId);

	/**
	 * 주어진 책 ID에 해당하는 모든 BookCategory 엔티티를 조회합니다.
	 *
	 * @param bookId 조회할 책의 ID
	 * @return 주어진 책 ID에 해당하는 모든 BookCategory 엔티티의 리스트
	 */
	List<BookCategory> findAllByBookBookId(Long bookId);
}
