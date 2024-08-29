package com.nhnacademy.bookstoreback.author.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.author.domain.dto.respnse.AuthorDto;
import com.nhnacademy.bookstoreback.author.domain.entity.Author;
import com.nhnacademy.bookstoreback.author.exception.AuthorAlreadyExistsException;
import com.nhnacademy.bookstoreback.author.exception.AuthorNotFoundException;
import com.nhnacademy.bookstoreback.author.repository.AuthorRepository;
import com.nhnacademy.bookstoreback.author.service.AuthorService;

import lombok.RequiredArgsConstructor;

/**
 * @author 김기욱, 이경헌
 * 작가 Service
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AuthorServiceImpl implements AuthorService {
	private final AuthorRepository authorRepository;

	/**
	 *{@inheritDoc}
	 */
	@Override
	public Author findOrCreateAuthor(String authorName) {
		Optional<Author> optionalAuthor = authorRepository.findByAuthorName(authorName);
		return optionalAuthor.orElseGet(() -> authorRepository.save(Author.builder().authorName(authorName).build()));
	}

	/**
	 *{@inheritDoc}
	 */
	@Transactional(readOnly = true)
	@Override
	public Page<AuthorDto> getAuthors(Pageable pageable) {
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();
		return authorRepository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "authorId")))
			.map(AuthorDto::fromEntity);
	}

	/**
	 *{@inheritDoc}
	 */
	@Transactional(readOnly = true)
	@Override
	public AuthorDto getAuthor(Long authorId) {
		Author author = authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotFoundException(authorId));
		return AuthorDto.fromEntity(author);
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void createAuthor(AuthorDto request) {
		if (authorRepository.existsByAuthorName(request.authorName())) {
			throw new AuthorAlreadyExistsException(request.authorName());
		}
		authorRepository.save(Author.toEntity(request));
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void updateAuthor(Long authorId, AuthorDto request) {
		Author author = authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotFoundException(authorId));

		if (authorRepository.existsByAuthorName(request.authorName())) {
			throw new AuthorAlreadyExistsException(request.authorName());
		}

		author.updateAuthorName(request.authorName());
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void deleteAuthor(Long authorId) {
		authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotFoundException(authorId));
		authorRepository.deleteById(authorId);
	}
}
