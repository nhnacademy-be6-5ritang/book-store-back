package com.nhnacademy.bookstoreback.category.domain.entity;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * 도서 카테고리 매핑 Entity
 *
 * @author 김기욱
 * @version 1.0
 */
@Entity
@Getter
@Table(name = "books_and_categories")
public class BookCategory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_category_id")
	private Long bookCategoryId;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "book_id")
	private Book book;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "category_id")
	private Category category;

	public BookCategory(Book book, Category category) {
		this.book = book;
		this.category = category;
	}
}