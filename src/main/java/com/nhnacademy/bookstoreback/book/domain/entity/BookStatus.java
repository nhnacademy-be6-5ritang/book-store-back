package com.nhnacademy.bookstoreback.book.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "books_statuses")
public class BookStatus {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_status_id")
	private Long bookStatusId;

	@Column(name = "book_status_name", nullable = false, length = 10)
	private BookStatusState bookStatusName = BookStatusState.ON_SALE;
}