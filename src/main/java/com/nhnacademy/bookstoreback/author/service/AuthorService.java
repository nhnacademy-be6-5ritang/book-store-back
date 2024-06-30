package com.nhnacademy.bookstoreback.author.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.author.domain.dto.respnse.AuthorDto;
import com.nhnacademy.bookstoreback.author.domain.entity.Author;
import com.nhnacademy.bookstoreback.author.repository.AuthorRepository;
import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

import lombok.RequiredArgsConstructor;

/**
 * 작가 Service
 *
 * @author 김기욱
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AuthorService {
	private final AuthorRepository authorRepository;

	/**
	 * 작가 이름 기반 도서 조회.
	 *
	 * @param authorName 작가 이름
	 * @return 작가가 존재하면 작가 정보, 없으면 null
	 */
	public Author findOrCreateAuthor(String authorName) {
		Optional<Author> optionalAuthor = authorRepository.findByAuthorName(authorName);
		return optionalAuthor.orElseGet(() -> authorRepository.save(Author.builder().authorName(authorName).build()));
	}

	@Transactional(readOnly = true)
	public List<AuthorDto> getAuthors() {
		return authorRepository.findAll().stream().map(AuthorDto::fromEntity).toList();
	}

	@Transactional(readOnly = true)
	public AuthorDto getAuthor(Long authorId) {
		Author author = authorRepository.findById(authorId).orElseThrow(() -> {
			String errorMessage = String.format("해당 작가 '%d'는 존재하지 않는 작가 입니다.", authorId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		return AuthorDto.fromEntity(author);
	}

	public AuthorDto createAuthor(AuthorDto request) {
		if (authorRepository.existsByAuthorName(request.authorName())) {
			String errorMessage = String.format("해당 작가 '%s'는 이미 존재 하는 작가 입니다.", request.authorName());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new AlreadyExistsException(errorStatus);
		}

		return AuthorDto.fromEntity(authorRepository.save(Author.toEntity(request)));
	}

	public AuthorDto updateAuthor(Long authorId, AuthorDto request) {
		Author author = authorRepository.findById(authorId).orElseThrow(() -> {
			String errorMessage = String.format("해당 작가 '%d'는 존재하지 않는 작가 입니다.", authorId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		if (authorRepository.existsByAuthorName(request.authorName())) {
			String errorMessage = String.format("해당 작가 '%s'는 이미 존재 하는 작가 입니다.", request.authorName());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new AlreadyExistsException(errorStatus);
		}

		author.updateAuthorName(request.authorName());

		authorRepository.save(author);

		return AuthorDto.fromEntity(author);
	}

	public void deleteAuthor(Long authorId) {
		authorRepository.findById(authorId).orElseThrow(() -> {
			String errorMessage = String.format("해당 작가 '%d'는 존재하지 않는 작가 입니다.", authorId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		authorRepository.deleteById(authorId);
	}
}
