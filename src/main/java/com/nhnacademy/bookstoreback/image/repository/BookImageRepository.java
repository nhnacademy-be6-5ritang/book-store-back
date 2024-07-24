package com.nhnacademy.bookstoreback.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.domain.entity.BookImage;
import com.nhnacademy.bookstoreback.image.domain.entity.Image;

/**
 * @author 김기욱
 * 도서 이미지 엔티티를 관리하는 리포지토리 인터페이스입니다.
 */
public interface BookImageRepository extends JpaRepository<BookImage, Long> {
	/**
	 * 주어진 {@code Book} 엔티티와 {@code Image} 엔티티에 대한 {@code BookImage} 엔티티의 존재 여부를 확인합니다.
	 *
	 * @param book 확인할 {@code Book} 엔티티
	 * @param image 확인할 {@code Image} 엔티티
	 * @return 주어진 책과 이미지 조합의 {@code BookImage} 엔티티 존재 여부
	 */
	boolean existsByBookAndImage(Book book, Image image);

	/**
	 * 주어진 책 ID에 해당하는 {@code Book} 엔티티와 연관된 모든 {@code BookImage} 엔티티를 삭제합니다.
	 *
	 * @param bookId 삭제할 {@code Book} 엔티티의 ID
	 */
	void deleteAllByBookBookId(Long bookId);
}
