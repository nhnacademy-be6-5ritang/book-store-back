package com.nhnacademy.bookstoreback.author.service.impl;

import java.util.List;
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
 * 작가 Service
 *
 * @author 김기욱
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AuthorServiceImpl implements AuthorService {
	private final AuthorRepository authorRepository;

	/**
	 * 작가 이름 기반 도서 조회.
	 *
	 * @param authorName 작가 이름
	 * @return 작가가 존재하면 작가 정보, 없으면 null
	 */
	@Override
	public Author findOrCreateAuthor(String authorName) {
		Optional<Author> optionalAuthor = authorRepository.findByAuthorName(authorName);
		return optionalAuthor.orElseGet(() -> authorRepository.save(Author.builder().authorName(authorName).build()));
	}

	@Transactional(readOnly = true)
	@Override
	public List<AuthorDto> getAuthors() {
		return authorRepository.findAll().stream().map(AuthorDto::fromEntity).toList();
	}

	@Transactional(readOnly = true)
	@Override
	public Page<AuthorDto> getAuthors(Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 10;
		return authorRepository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "authorId")))
			.map(AuthorDto::fromEntity);
	}

	@Transactional(readOnly = true)
	@Override
	public AuthorDto getAuthor(Long authorId) {
		Author author = authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotFoundException(authorId));
		return AuthorDto.fromEntity(author);
	}

	@Override
	public AuthorDto createAuthor(AuthorDto request) {
		if (authorRepository.existsByAuthorName(request.authorName())) {
			throw new AuthorAlreadyExistsException(request.authorName());
		}

		return AuthorDto.fromEntity(authorRepository.save(Author.toEntity(request)));
	}

	@Override
	public AuthorDto updateAuthor(Long authorId, AuthorDto request) {
		Author author = authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotFoundException(authorId));

		if (authorRepository.existsByAuthorName(request.authorName())) {
			throw new AuthorAlreadyExistsException(request.authorName());
		}

		author.updateAuthorName(request.authorName());
		return AuthorDto.fromEntity(author);
	}

	@Override
	public void deleteAuthor(Long authorId) {
		authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotFoundException(authorId));
		authorRepository.deleteById(authorId);
	}
}
