package com.nhnacademy.bookstoreback.book.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * 태그 Entity
 *
 * @author 김기욱
 * @version 1.0
 */
@Entity
@Getter
@Table(name = "tags")
public class Tag {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tag_id")
	private Long tagId;

	@NotNull
	@Column(name = "tag_name", length = 20)
	private String tagName;
}