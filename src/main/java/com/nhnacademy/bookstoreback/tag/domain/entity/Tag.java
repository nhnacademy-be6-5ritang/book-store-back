package com.nhnacademy.bookstoreback.tag.domain.entity;

import com.nhnacademy.bookstoreback.tag.domain.dto.respnse.TagDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 태그 Entity
 *
 * @author 김기욱
 * @version 1.0
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tags")
public class Tag {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tag_id")
	private Long tagId;

	@NotNull
	@Column(name = "tag_name", length = 20)
	private String tagName;

	@Builder
	public Tag(String tagName) {
		this.tagName = tagName;
	}

	public static Tag toEntity(TagDto request) {
		return Tag.builder()
			.tagName(request.tagName())
			.build();
	}

	public void updateTagName(String tagName) {
		this.tagName = tagName;
	}
}