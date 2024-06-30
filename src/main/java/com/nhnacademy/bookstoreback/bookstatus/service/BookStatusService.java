package com.nhnacademy.bookstoreback.bookstatus.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.bookstatus.domain.dto.respnse.BookStatusDto;
import com.nhnacademy.bookstoreback.bookstatus.domain.entity.BookStatus;
import com.nhnacademy.bookstoreback.bookstatus.repository.BookStatusRepository;
import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

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
public class BookStatusService {
	private final EntityManager entityManager;
	private final BookStatusRepository bookStatusRepository;

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
	public List<BookStatusDto> getBookStatuses() {
		return bookStatusRepository.findAll().stream().map(BookStatusDto::fromEntity).toList();
	}

	@Transactional(readOnly = true)
	public BookStatusDto getBookStatus(Long bookStatusId) {
		BookStatus bookStatus = bookStatusRepository.findById(bookStatusId).orElseThrow(() -> {
			String errorMessage = String.format("해당 도서상태 '%d'는 존재하지 않는 도서상태 입니다.", bookStatusId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		return BookStatusDto.fromEntity(bookStatus);
	}

	public BookStatusDto createBookStatus(BookStatusDto request) {
		if (bookStatusRepository.existsByBookStatusName(request.bookStatusName())) {
			String errorMessage = String.format("해당 도서상태 '%s'는 이미 존재 하는 도서상태 입니다.", request.bookStatusName());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new AlreadyExistsException(errorStatus);
		}

		return BookStatusDto.fromEntity(bookStatusRepository.save(BookStatus.toEntity(request)));
	}

	public BookStatusDto updateBookStatus(Long bookStatusId, BookStatusDto request) {
		BookStatus bookStatus = bookStatusRepository.findById(bookStatusId).orElseThrow(() -> {
			String errorMessage = String.format("해당 도서상태 '%d'는 존재하지 않는 도서상태 입니다.", bookStatusId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		if (bookStatusRepository.existsByBookStatusName(request.bookStatusName())) {
			String errorMessage = String.format("해당 도서상태 '%s'는 이미 존재 하는 도서상태 입니다.", request.bookStatusName());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new AlreadyExistsException(errorStatus);
		}

		bookStatus.updateBookStatusName(request.bookStatusName());

		bookStatusRepository.save(bookStatus);

		return BookStatusDto.fromEntity(bookStatus);
	}

	public void deleteBookStatus(Long bookStatusId) {
		bookStatusRepository.findById(bookStatusId).orElseThrow(() -> {
			String errorMessage = String.format("해당 도서상태 '%d'는 존재하지 않는 도서상태 입니다.", bookStatusId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		bookStatusRepository.deleteById(bookStatusId);
	}
}
