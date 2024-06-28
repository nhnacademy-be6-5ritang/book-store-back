package com.nhnacademy.bookstoreback.book.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nhnacademy.bookstoreback.book.domain.entity.Publisher;

/**
 * 출판사 Repository
 *
 * @author 김기욱
 * @version 1.0
 */
@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
	/**
	 * 출판사 이름 기반 도서 조회
	 *
	 * @param publisherName 출판사 이름
	 * @return 도서 정보 (Optional로 반환)
	 */
	Optional<Publisher> findByPublisherName(String publisherName);
}
