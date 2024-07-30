package com.nhnacademy.bookstoreback.publisher.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.publisher.domain.entity.Publisher;

/**
 * @author 김기욱, 이경헌
 * Publisher 엔티티를 관리하는 Spring Data JPA 리포지토리입니다.
 */
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
	/**
	 * 출판사 이름 기반 도서 조회
	 *
	 * @param publisherName 출판사 이름
	 * @return 도서 정보 (Optional로 반환)
	 */
	Optional<Publisher> findByPublisherName(String publisherName);

	/**
	 * 주어진 출판사 이름이 존재하는지 확인합니다.
	 *
	 * @param publisherName 확인할 출판사 이름
	 * @return 출판사 이름이 존재하면 {@code true}, 그렇지 않으면 {@code false}
	 */
	boolean existsByPublisherName(String publisherName);
}
