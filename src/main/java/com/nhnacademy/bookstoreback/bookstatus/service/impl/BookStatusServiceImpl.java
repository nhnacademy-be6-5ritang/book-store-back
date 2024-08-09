package com.nhnacademy.bookstoreback.bookstatus.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.bookstatus.domain.dto.respnse.BookStatusDto;
import com.nhnacademy.bookstoreback.bookstatus.domain.entity.BookStatus;
import com.nhnacademy.bookstoreback.bookstatus.exception.BookStatusAlreadyExistsException;
import com.nhnacademy.bookstoreback.bookstatus.exception.BookStatusNotFoundException;
import com.nhnacademy.bookstoreback.bookstatus.repository.BookStatusRepository;
import com.nhnacademy.bookstoreback.bookstatus.service.BookStatusService;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

/**
 * @author 김기욱
 * 도서 상태 Service
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BookStatusServiceImpl implements BookStatusService {
	private final EntityManager entityManager;
	private final BookStatusRepository bookStatusRepository;

	/**
	 *{@inheritDoc}
	 */
	@Override
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
	 *{@inheritDoc}
	 */
	@Override
	public BookStatus findOrCreateBookStatus(String bookStatusName) {
		Optional<BookStatus> optionalStatus = findByBookStatusName(bookStatusName);

		return optionalStatus.orElseGet(() -> {
			BookStatus newStatus = new BookStatus(bookStatusName);
			entityManager.persist(newStatus);
			return newStatus;
		});
	}

	/**
	 *{@inheritDoc}
	 */
	@Transactional(readOnly = true)
	@Override
	public List<BookStatusDto> getBookStatuses() {
		return bookStatusRepository.findAll().stream().map(BookStatusDto::fromEntity).toList();
	}

	/**
	 *{@inheritDoc}
	 */
	@Transactional(readOnly = true)
	@Override
	public BookStatusDto getBookStatus(Long bookStatusId) {
		BookStatus bookStatus = bookStatusRepository.findById(bookStatusId)
			.orElseThrow(() -> new BookStatusNotFoundException(bookStatusId));
		return BookStatusDto.fromEntity(bookStatus);
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void createBookStatus(BookStatusDto request) {
		if (bookStatusRepository.existsByBookStatusName(request.bookStatusName())) {
			throw new BookStatusAlreadyExistsException(request.bookStatusName());
		}
		bookStatusRepository.save(BookStatus.toEntity(request));
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void updateBookStatus(Long bookStatusId, BookStatusDto request) {
		BookStatus bookStatus = bookStatusRepository.findById(bookStatusId)
			.orElseThrow(() -> new BookStatusNotFoundException(bookStatusId));

		if (bookStatusRepository.existsByBookStatusName(request.bookStatusName())) {
			throw new BookStatusAlreadyExistsException(request.bookStatusName());
		}
		bookStatus.updateBookStatusName(request.bookStatusName());
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void deleteBookStatus(Long bookStatusId) {
		bookStatusRepository.deleteById(bookStatusId);
	}
}
