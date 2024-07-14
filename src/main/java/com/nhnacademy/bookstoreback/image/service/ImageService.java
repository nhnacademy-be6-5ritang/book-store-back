package com.nhnacademy.bookstoreback.image.service;

import org.springframework.stereotype.Service;

import com.nhnacademy.bookstoreback.image.domain.entity.Image;
import com.nhnacademy.bookstoreback.image.repository.ImageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {
	private final ImageRepository imageRepository;

	public Image save(Image image) {
		return imageRepository.save(image);
	}
}
