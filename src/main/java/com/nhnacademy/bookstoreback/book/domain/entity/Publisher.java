package com.nhnacademy.bookstoreback.book.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 출판사 Entity
 *
 * @author 김기욱
 * @version 1.0
 */
@Entity
@Getter
@Setter
@Table(name = "publishers")
public class Publisher {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "publisher_id")
	private Long publisherId;

	@NotNull
	@Column(name = "publisher_name", length = 50)
	private String publisherName;
}