package com.nhnacademy.bookstoreback.image.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "images")
@Setter
public class Image {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "image_id")
	private Long imageId;

	@NotBlank
	@Size(max = 300)
	@Column(name = "image_name", nullable = false, length = 300)
	private String imageName;

	@NotBlank
	@Size(max = 300)
	@Column(name = "image_url", nullable = false, length = 300)
	private String imageUrl;

	@NotNull
	@Column(name = "image_created_at")
	private LocalDateTime imageCreatedAt = LocalDateTime.now();

	@Builder
	public Image(String fileName, String imageUrl) {
		this.imageName = fileName;
		this.imageUrl = imageUrl;
	}
}
