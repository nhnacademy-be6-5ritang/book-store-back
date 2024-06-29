package com.nhnacademy.bookstoreback.book.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.book.domain.entity.BookStatus;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

/**
 * 도서 상태 Service
 *
 * @author 김기욱
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class BookStatusService {
	private final EntityManager entityManager;

	/**
	 * 도서 상태 이름 기반 도서 조회
	 *
	 * @param bookStatusName 도서 상태 이름
	 * @return 도서 상태 (Optional로 반환)
	 */
	public Optional<BookStatus> findByBookStatusName(String bookStatusName) {
		List<BookStatus> results = entityManager
			.createQuery("SELECT b FROM BookStatus b WHERE b.bookStatusName = :bookStatusName", BookStatus.class)
			.setParameter("bookStatusName", bookStatusName)
			.getResultList();

		if (results.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(results.get(0));
		}
	}

	/**
	 * 도서 상태 생성 또는 조회
	 *
	 * @param bookStatusName 도서 상태 이름
	 * @return 도서 상태가 존재하면 도서 상태 정보, 없으면 생성된 도서 상태 정보
	 */
	@Transactional
	public BookStatus findOrCreateBookStatus(String bookStatusName) {
		Optional<BookStatus> optionalStatus = findByBookStatusName(bookStatusName);

		return optionalStatus.orElseGet(() -> {
			BookStatus newStatus = new BookStatus();
			newStatus.setBookStatusName(bookStatusName);
			entityManager.persist(newStatus);
			return newStatus;
		});
	}
}
