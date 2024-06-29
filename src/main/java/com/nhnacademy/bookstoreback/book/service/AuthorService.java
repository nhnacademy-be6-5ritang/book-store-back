package com.nhnacademy.bookstoreback.book.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.book.domain.entity.Author;
import com.nhnacademy.bookstoreback.book.repository.AuthorRepository;

import lombok.RequiredArgsConstructor;

/**
 * 작가 Service
 *
 * @author 김기욱
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class AuthorService {
	private final AuthorRepository authorRepository;

	/**
	 * 작가 이름 기반 도서 조회.
	 *
	 * @param authorName 작가 이름
	 * @return 작가가 존재하면 작가 정보, 없으면 null
	 */
	@Transactional
	public Author findOrCreateAuthor(String authorName) {
		Optional<Author> optionalAuthor = authorRepository.findByAuthorName(authorName);
		return optionalAuthor.orElseGet(() -> authorRepository.save(Author.builder().authorName(authorName).build()));
	}
}
