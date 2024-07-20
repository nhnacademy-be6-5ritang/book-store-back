package com.nhnacademy.bookstoreback.category.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;

/**
 * 도서 카테고리 매핑 Entity
 *
 * @version 1.0
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "books_and_categories")
@Document(indexName = "books_and_categories")
public class BookCategory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_category_id")
	private Long bookCategoryId;

	@ManyToOne(optional = false)
	@NotNull
	@JoinColumn(name = "book_id")
	@Field(type = FieldType.Long)
	private Book book;

	@ManyToOne(optional = false)
	@NotNull
	@JoinColumn(name = "category_id")
	@Field(type = FieldType.Long)
	private Category category;

	public BookCategory(Book book, Category category) {
		this.book = book;
		this.category = category;
	}
}
