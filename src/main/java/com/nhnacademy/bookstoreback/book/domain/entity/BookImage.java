package com.nhnacademy.bookstoreback.book.domain.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;

@Entity
@Getter
@Table(name = "books_images")
public class BookImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_image_id")
	private Long bookImageId;

	@ManyToOne
	@JoinColumn(name = "book_id", nullable = false)
	private Book book;

	@Column(name = "book_image_name", nullable = false, length = 100)
	private String bookImageName;

	@Column(name = "book_image_url", nullable = false, length = 100)
	private String bookImageUrl;

	@Column(name = "book_image_created_at", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date bookImageCreatedAt;
}