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
	@JoinColumn(name = "image_id")
	private Long imageId;

	@Column(name = "image_name", length = 300)
	@NotNull
	private String imageName;

	@Column(name = "image_url", length = 300)
	@NotNull
	private String imageUrl;

	@Column(name = "image_created_at")
	@NotNull
	private LocalDateTime imageCreatedAt = LocalDateTime.now();

	@Builder
	public Image(String fileName, String imageUrl) {
		this.imageName = fileName;
		this.imageUrl = imageUrl;
	}
}
