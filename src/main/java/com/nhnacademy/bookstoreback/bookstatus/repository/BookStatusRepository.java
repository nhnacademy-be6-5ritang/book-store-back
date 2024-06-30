package com.nhnacademy.bookstoreback.bookstatus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nhnacademy.bookstoreback.bookstatus.domain.entity.BookStatus;

/**
 * 도서 상태 Repository
 *
 * @author 김기욱
 * @version 1.0
 */
@Repository
public interface BookStatusRepository extends JpaRepository<BookStatus, Long> {
	boolean existsByBookStatusName(String bookStatusName);
}