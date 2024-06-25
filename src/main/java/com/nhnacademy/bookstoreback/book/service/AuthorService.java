package com.nhnacademy.bookstoreback.book.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.book.domain.entity.Author;
import com.nhnacademy.bookstoreback.book.repository.AuthorRepository;

/**
 * @author 김기욱
 * @version 1.0
 */
@Service
public class AuthorService {

	private final AuthorRepository authorRepository;

	@Autowired
	public AuthorService(AuthorRepository authorRepository) {
		this.authorRepository = authorRepository;
	}

	@Transactional
	public Author findOrCreateAuthor(String authorName) {
		return authorRepository.findByAuthorName(authorName).orElseGet(() -> {
			Author newAuthor = new Author();
			newAuthor.setAuthorName(authorName);
			return authorRepository.save(newAuthor);
		});
	}
}
