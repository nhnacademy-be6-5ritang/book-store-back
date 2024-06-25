package com.nhnacademy.bookstoreback.book.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.book.domain.entity.BookStatus;

import jakarta.persistence.EntityManager;

/**
 * @author 김기욱
 * @version 1.0
 */
@Service
public class BookStatusService {
	private final EntityManager entityManager;

	@Autowired
	public BookStatusService(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

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
