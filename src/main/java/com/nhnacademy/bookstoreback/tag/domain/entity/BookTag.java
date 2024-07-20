package com.nhnacademy.bookstoreback.tag.domain.entity;

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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 도서 태그 매핑 Entity
 *
 * @version 1.0
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "books_and_tags")
@Document(indexName = "books_and_tags")
public class BookTag {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_tag_id")
	private Long bookTagId;

	@ManyToOne(optional = false)
	@NotNull
	@JoinColumn(name = "book_id")
	@Field(type = FieldType.Long)
	private Book book;

	@ManyToOne(optional = false)
	@NotNull
	@JoinColumn(name = "tag_id")
	@Field(type = FieldType.Long)
	private Tag tag;

	public BookTag(Book book, Tag tag) {
		this.book = book;
		this.tag = tag;
	}
}
