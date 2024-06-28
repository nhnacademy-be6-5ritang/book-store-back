package com.nhnacademy.bookstoreback.book.domain.entity;

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
 * 도서 태그 매핑 Entity
 *
 * @author 김기욱
 * @version 1.0
 */
@Entity
@Getter
@Table(name = "books_and_tags")
public class BookTag {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_tag_id")
	private Long bookTagId;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "book_id")
	private Book book;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "tag_id")
	private Tag tag;
}