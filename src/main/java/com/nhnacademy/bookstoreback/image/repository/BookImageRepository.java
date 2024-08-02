package com.nhnacademy.bookstoreback.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.book.domain.entity.BookImage;

/**
 * @author 김기욱
 * 도서 이미지 엔티티를 관리하는 리포지토리 인터페이스입니다.
 */
public interface BookImageRepository extends JpaRepository<BookImage, Long> {

	/**
	 * 주어진 책 ID에 해당하는 {@code Book} 엔티티와 연관된 모든 {@code BookImage} 엔티티를 삭제합니다.
	 *
	 * @param bookId 삭제할 {@code Book} 엔티티의 ID
	 */
	void deleteAllByBookBookId(Long bookId);
}
