package com.nhnacademy.bookstoreback.book.repository.impl;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import com.nhnacademy.bookstoreback.author.domain.entity.Author;
import com.nhnacademy.bookstoreback.book.domain.dto.response.BookSearchResult;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.bookstatus.domain.entity.BookStatus;
import com.nhnacademy.bookstoreback.config.QuerydslTestConfig;
import com.nhnacademy.bookstoreback.publisher.domain.entity.Publisher;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@DataJpaTest
@Import(QuerydslTestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CustomBookRepositoryImplTest {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	private CustomBookRepositoryImpl customBookRepository;

	@BeforeEach
	void setUp() {
		customBookRepository = new CustomBookRepositoryImpl(entityManager);

		BookStatus bookStatus = BookStatus.builder().bookStatusName("Available").build();
		entityManager.persist(bookStatus);

		Author author = Author.builder().authorName("sam").build();
		entityManager.persist(author);

		Publisher publisher = Publisher.builder().publisherName("kim").build();
		entityManager.persist(publisher);

		Book book1 = Book.builder()
			.bookTitle("Sample Book One")
			.bookDescription("Sample Book One")
			.bookPrice(BigDecimal.valueOf(10000))
			.bookQuantity(100)
			.bookStatus(bookStatus)
			.author(author)
			.bookIsbn("1234")
			.bookPublishDate(Date.from(Instant.now()))
			.bookSalePercent(BigDecimal.valueOf(0.8))
			.bookSalePrice(BigDecimal.valueOf(1000))
			.publisher(publisher)
			.build();

		Book book2 = Book.builder()
			.bookTitle("Sample Book two")
			.bookDescription("Sample Book Two")
			.bookPrice(BigDecimal.valueOf(10000))
			.bookQuantity(100)
			.bookStatus(bookStatus)
			.author(author)
			.bookIsbn("5678")
			.bookPublishDate(Date.from(Instant.now()))
			.bookSalePercent(BigDecimal.valueOf(0.8))
			.bookSalePrice(BigDecimal.valueOf(1000))
			.publisher(publisher)
			.build();

		entityManager.persist(book1);
		entityManager.persist(book2);
	}

	@Test
	void findByBookTitleContainingIgnoreCaseCustom() {
		// Given
		String title = "sample";

		// When
		List<BookSearchResult> results = customBookRepository.findByBookTitleContainingIgnoreCaseCustom(title);

		// Then
		assertThat(results).hasSize(2);
		assertThat(results).extracting(BookSearchResult::bookId).containsExactly(1L, 2L);
		assertThat(results).extracting(BookSearchResult::bookTitle).containsExactly("Sample Book One", "Sample Book two");
	}

	@Test
	void testFindByBookTitleContainingDifferentCase() {
		// Given
		String title = "SAMPLE";

		// When
		List<BookSearchResult> results = customBookRepository.findByBookTitleContainingIgnoreCaseCustom(title);

		// Then
		assertThat(results).hasSize(2);
		assertThat(results).extracting(BookSearchResult::bookId)
			.containsExactlyInAnyOrder(1L, 2L); // 순서에 상관없이 정확한 ID 값을 확인
		assertThat(results).extracting(BookSearchResult::bookTitle).containsExactly("Sample Book One", "Sample Book two");
	}

	@Test
	void testFindByBookTitleWithNoMatchingResults() {
		// Given
		String title = "Nonexistent Title";

		// When
		List<BookSearchResult> results = customBookRepository.findByBookTitleContainingIgnoreCaseCustom(title);

		// Then
		assertThat(results).isEmpty();
	}

	@Test
	void testFindByBookTitleWithPartialMatch() {
		// Given
		String title = "Book";

		// When
		List<BookSearchResult> results = customBookRepository.findByBookTitleContainingIgnoreCaseCustom(title);

		// Then
		assertThat(results).hasSize(2);
		assertThat(results).extracting(BookSearchResult::bookTitle).containsExactlyInAnyOrder("Sample Book One", "Sample Book two");
	}
}