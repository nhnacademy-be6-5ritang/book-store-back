package com.nhnacademy.bookstoreback.book.domain.entity;

import com.nhnacademy.bookstoreback.image.domain.entity.Image;

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
 * 도서 이미지 Entity
 *
 * @author 김기욱
 * @version 1.0
 */
@Entity
@Getter
@Table(name = "books_images")
public class BookImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_image_id")
	private Long bookImageId;

	@ManyToOne
	@JoinColumn(name = "book_id")
	@NotNull
	private Book book;

	@ManyToOne
	@JoinColumn(name = "image_id")
	@NotNull
	private Image image;
}