package com.nhnacademy.bookstoreback.book.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books_statuses")
public class BookStatus {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_status_id")
	private Long bookStatusId;

	@Column(name = "book_status_name", nullable = false, length = 10)    // ON_SALE, SOLD_OUT, DELETED, UNKNOWN
	private String bookStatusName;

	public BookStatus(String bookStatusName) {
		this.bookStatusName = bookStatusName;
	}
}
