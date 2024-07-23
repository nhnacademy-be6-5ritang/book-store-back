package com.nhnacademy.bookstoreback.author.domain.entity;

import com.nhnacademy.bookstoreback.author.domain.dto.respnse.AuthorDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 작가 Entity
 *
 * @author 김기욱
 * @version 1.0
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "authors")
public class Author {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "author_id")
	private Long authorId;

	@NotBlank
	@Size(min = 1, max = 200)
	@Field(type = FieldType.Text, analyzer = "nori_analyzer") // Nori 분석기 적용
	@Column(name = "author_name", length = 200)
	private String authorName;

	@Builder
	public Author(String authorName) {
		this.authorName = authorName;
	}

	public static Author toEntity(AuthorDto request) {
		return Author.builder()
			.authorName(request.authorName())
			.build();
	}

	public void updateAuthorName(String authorName) {
		this.authorName = authorName;
	}
}
