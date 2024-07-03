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
 * 도서 상태 Service
 *
 * @author 김기욱
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BookStatusServiceImpl implements BookStatusService {
	private final EntityManager entityManager;
	private final BookStatusRepository bookStatusRepository;

	/**
	 * 도서 상태 이름 기반 도서 조회
	 *
	 * @param bookStatusName 도서 상태 이름
	 * @return 도서 상태 (Optional로 반환)
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
	 * 도서 상태 생성 또는 조회
	 *
	 * @param bookStatusName 도서 상태 이름
	 * @return 도서 상태가 존재하면 도서 상태 정보, 없으면 생성된 도서 상태 정보
	 */
	@Override
	public BookStatus findOrCreateBookStatus(String bookStatusName) {
		Optional<BookStatus> optionalStatus = findByBookStatusName(bookStatusName);

		return optionalStatus.orElseGet(() -> {
			BookStatus newStatus = new BookStatus();
			newStatus.setBookStatusName(bookStatusName);
			entityManager.persist(newStatus);
			return newStatus;
		});
	}

	@Transactional(readOnly = true)
	@Override
	public List<BookStatusDto> getBookStatuses() {
		return bookStatusRepository.findAll().stream().map(BookStatusDto::fromEntity).toList();
	}

	@Transactional(readOnly = true)
	@Override
	public BookStatusDto getBookStatus(Long bookStatusId) {
		BookStatus bookStatus = bookStatusRepository.findById(bookStatusId)
			.orElseThrow(() -> new BookStatusNotFoundException(bookStatusId));
		return BookStatusDto.fromEntity(bookStatus);
	}

	@Override
	public BookStatusDto createBookStatus(BookStatusDto request) {
		if (bookStatusRepository.existsByBookStatusName(request.bookStatusName())) {
			throw new BookStatusAlreadyExistsException(request.bookStatusName());
		}
		return BookStatusDto.fromEntity(bookStatusRepository.save(BookStatus.toEntity(request)));
	}

	@Override
	public BookStatusDto updateBookStatus(Long bookStatusId, BookStatusDto request) {
		BookStatus bookStatus = bookStatusRepository.findById(bookStatusId)
			.orElseThrow(() -> new BookStatusNotFoundException(bookStatusId));

		if (bookStatusRepository.existsByBookStatusName(request.bookStatusName())) {
			throw new BookStatusAlreadyExistsException(request.bookStatusName());
		}
		bookStatus.updateBookStatusName(request.bookStatusName());
		return BookStatusDto.fromEntity(bookStatus);
	}

	@Override
	public void deleteBookStatus(Long bookStatusId) {
		bookStatusRepository.findById(bookStatusId).orElseThrow(() -> new BookStatusNotFoundException(bookStatusId));
		bookStatusRepository.deleteById(bookStatusId);
	}
}
