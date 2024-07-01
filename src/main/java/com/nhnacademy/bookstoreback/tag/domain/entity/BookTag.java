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
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 도서 태그 매핑 Entity
 *
 * @author 김기욱
 * @version 1.0
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "books_and_tags")
public class BookTag {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_tag_id")
	private Long bookTagId;

	@ManyToOne(optional = false)
	@NotNull
	@JoinColumn(name = "book_id")
	private Book book;

	@ManyToOne(optional = false)
	@NotNull
	@JoinColumn(name = "tag_id")
	private Tag tag;

	public BookTag(Book book, Tag tag) {
		this.book = book;
		this.tag = tag;
	}
}