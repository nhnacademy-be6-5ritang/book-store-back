package com.nhnacademy.bookstoreback.publisher.domain.entity;

import com.nhnacademy.bookstoreback.publisher.domain.dto.respnse.PublisherDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 출판사 Entity
 *
 * @author 김기욱
 * @version 1.0
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "publishers")
public class Publisher {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "publisher_id")
	private Long publisherId;

	@NotBlank
	@Size(max = 100)
	@Column(name = "publisher_name", length = 100)
	private String publisherName;

	@Builder
	public Publisher(String publisherName) {
		this.publisherName = publisherName;
	}

	public static Publisher toEntity(PublisherDto request) {
		return Publisher.builder()
			.publisherName(request.publisherName())
			.build();
	}

	public void updatePublisherName(String publisherName) {
		this.publisherName = publisherName;
	}
}
