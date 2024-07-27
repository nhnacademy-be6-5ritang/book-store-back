package com.nhnacademy.bookstoreback.book.repository.impl;

import static com.nhnacademy.bookstoreback.book.domain.entity.QBook.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nhnacademy.bookstoreback.book.domain.dto.response.BookSearchResult;
import com.nhnacademy.bookstoreback.book.repository.CustomBookRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class CustomBookRepositoryImpl implements CustomBookRepository {

	private final JPAQueryFactory queryFactory;

	public CustomBookRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<BookSearchResult> findByBookTitleContainingIgnoreCaseCustom(String title) {
		return queryFactory
			.select(book.bookId, book.bookTitle)
			.from(book)
			.where(book.bookTitle.toLowerCase().contains(title.toLowerCase()))
			.fetch()
			.stream()
			.map(b -> new BookSearchResult(b.get(book.bookId), b.get(book.bookTitle)))
			.toList();
	}
}



