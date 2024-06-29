package com.nhnacademy.bookstoreback.image.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "images")
public class Image {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JoinColumn(name = "image_id")
	private Long imageId;

	@Column(name = "image_name", length = 100)
	@NotNull
	private String imageName;

	@Column(name = "image_url", length = 300)
	@NotNull
	private String imageUrl;

	@Column(name = "image_created_at")
	@NotNull
	private LocalDateTime imageCreatedAt;
}